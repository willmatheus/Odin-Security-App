package com.android.odin.receiver

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.android.odin.AppInfoActivity
import com.android.odin.PermissionRiskClassifier
import com.android.odin.PermissionRiskClassifier.RiskLevel
import com.android.odin.R
import com.android.odin.viewmodel.InstalledAppsViewModel.Companion.APP_PACKAGE_NAME_EXTRA

class AppInstallReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "AppInstallReceiver"

        // Alterado para v2 para garantir que a nova importância (HIGH) seja aplicada
        private const val CHANNEL_ID = "app_risk_analysis_channel_v2"
        private const val CHANNEL_NAME = "Alertas de Segurança"
        private const val CHANNEL_DESCRIPTION =
            "Notificações críticas sobre o risco de aplicativos recém-instalados"
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        Log.d(TAG, "Broadcast recebido: $action")

        if (action != Intent.ACTION_PACKAGE_ADDED &&
            action != Intent.ACTION_PACKAGE_REPLACED
        ) {
            return
        }

        val packageName = intent.data?.schemeSpecificPart ?: return
        Log.i(TAG, "Analisando instalação de: $packageName")

        if (packageName == context.packageName) {
            return
        }

        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfoCompat(packageName)

            val result = PermissionRiskClassifier.classifyApp(
                packageInfo = packageInfo,
                packageManager = packageManager
            )

            showRiskNotification(
                context = context,
                packageName = packageName,
                appName = result.appName,
                riskLevel = result.riskLevel,
                finalRiskScore = result.finalRiskScore
            )
        } catch (exception: Exception) {
            Log.e(TAG, "Erro ao processar pacote $packageName. O APK pode não estar pronto.", exception)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showRiskNotification(
        context: Context,
        packageName: String,
        appName: String,
        riskLevel: RiskLevel,
        finalRiskScore: Double
    ) {
        createNotificationChannel(context)

        if (!canPostNotifications(context)) {
            Log.w(TAG, "Falha ao exibir notificação: Permissão POST_NOTIFICATIONS negada.")
            return
        }

        val intent = Intent(context, AppInfoActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(APP_PACKAGE_NAME_EXTRA, packageName)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            packageName.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // IMPORTANTE: setPriority(PRIORITY_HIGH) + IMPORTANCE_HIGH no canal = Banner (Heads-up)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Risco detectado: $appName")
            .setContentText("O app possui risco ${riskLevel.label()} (Índice: %.1f)".format(finalRiskScore))
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "O Odin analisou o aplicativo $appName. " +
                            "Nível de risco: ${riskLevel.label()}. Índice: %.1f. " +
                            "Toque para revisar as permissões e detalhes de segurança."
                )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(
                packageName.hashCode(),
                notification
            )
            Log.d(TAG, "Notificação enviada com sucesso para $packageName")
        } catch (e: SecurityException) {
            Log.e(TAG, "Erro de segurança ao postar notificação: ${e.message}")
        }
    }

    private fun createNotificationChannel(context: Context) {
        // Como o minSdk é 31, o canal é obrigatório e as APIs são nativas
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH // Garante que a notificação apareça como banner
        ).apply {
            description = CHANNEL_DESCRIPTION
            enableVibration(true)
        }

        val notificationManager =
            context.getSystemService(NotificationManager::class.java)

        notificationManager?.createNotificationChannel(channel)
    }

    private fun canPostNotifications(context: Context): Boolean {
        // Verifica se as notificações globais do app estão ligadas no sistema
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            return false
        }

        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun PackageManager.getPackageInfoCompat(packageName: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(
                    PackageManager.GET_PERMISSIONS.toLong()
                )
            )
        } else {
            @Suppress("DEPRECATION")
            getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        }

    private fun RiskLevel.label(): String {
        return when (this) {
            RiskLevel.LOW -> "Baixo"
            RiskLevel.MODERATE -> "Moderado"
            RiskLevel.HIGH -> "Alto"
            RiskLevel.CRITICAL -> "Crítico"
        }
    }
}
