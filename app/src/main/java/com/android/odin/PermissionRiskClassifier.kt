package com.android.odin

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import com.android.odin.data.AppRiskUiState

object PermissionRiskClassifier {

    private const val PLAY_STORE_PACKAGE = "com.android.vending"
    private val TAG = PermissionRiskClassifier::class.java.name

    enum class RiskLevel {
        LOW,
        MODERATE,
        HIGH,
        CRITICAL
    }

    enum class InstallOrigin {
        GOOGLE_PLAY,
        UNKNOWN,
        EXTERNAL_APK
    }

    data class PermissionContribution(
        val permission: String,
        val severity: Severity,
        val weight: Double,
        val stateFactor: Double,
        val contribution: Double,
        val description: String
    )

    fun classifyApp(
        packageInfo: PackageInfo,
        packageManager: PackageManager
    ): AppRiskUiState {
        val packageName = packageInfo.packageName
        val appName = packageInfo.applicationInfo?.loadLabel(packageManager).toString()
        val appIcon = packageInfo.applicationInfo?.loadIcon(packageManager)
        val permissions = packageInfo.requestedPermissions ?: emptyArray()
        val permissionFlags = packageInfo.requestedPermissionsFlags ?: IntArray(0)

        val installOrigin = getInstallOrigin(packageManager, packageName)

        val contributions = permissions.mapIndexedNotNull { index, permission ->
            val risk = permissionRiskMap[permission] ?: return@mapIndexedNotNull null

            val stateFactor = if (isPermissionGranted(permissionFlags, index)) {
                1.0
            } else {
                0.5
            }

            PermissionContribution(
                permission = risk.permission,
                severity = risk.severity,
                weight = risk.weight,
                stateFactor = stateFactor,
                contribution = risk.weight * stateFactor,
                description = risk.description
            )
        }.sortedByDescending { it.contribution }

        val baseRiskScore = contributions.sumOf { it.contribution }
        val originFactor = getOriginFactor(installOrigin)
        val finalRiskScore = baseRiskScore * originFactor

        return AppRiskUiState(
            packageName = packageName,
            appName = appName,
            appIcon = appIcon,
            baseRiskScore = baseRiskScore,
            finalRiskScore = finalRiskScore,
            riskLevel = classifyRiskLevel(finalRiskScore),
            installOrigin = installOrigin,
            contributingPermissions = contributions
        )
    }

    private fun isPermissionGranted(
        permissionFlags: IntArray,
        index: Int
    ): Boolean {
        if (index !in permissionFlags.indices) {
            return false
        }

        return permissionFlags[index] and
                PackageInfo.REQUESTED_PERMISSION_GRANTED != 0
    }

    private fun getInstallOrigin(
        packageManager: PackageManager,
        packageName: String
    ): InstallOrigin {
        return try {
            val sourceInfo = packageManager.getInstallSourceInfo(packageName)

            val installerPackageName =
                sourceInfo.initiatingPackageName ?: sourceInfo.installingPackageName

            Log.d(TAG, "package: $packageName | installer: $installerPackageName")

            when (installerPackageName) {
                PLAY_STORE_PACKAGE -> InstallOrigin.GOOGLE_PLAY
                null -> InstallOrigin.UNKNOWN
                else -> InstallOrigin.EXTERNAL_APK
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Erro ao verificar origem do app: $packageName", exception)
            InstallOrigin.UNKNOWN
        }
    }

    private fun getOriginFactor(origin: InstallOrigin): Double {
        return when (origin) {
            InstallOrigin.GOOGLE_PLAY -> 1.0
            InstallOrigin.UNKNOWN -> 1.2
            InstallOrigin.EXTERNAL_APK -> 1.3
        }
    }

    private fun classifyRiskLevel(finalRiskScore: Double): RiskLevel {
        return when {
            finalRiskScore <= 10.0 -> RiskLevel.LOW
            finalRiskScore <= 25.0 -> RiskLevel.MODERATE
            finalRiskScore <= 45.0 -> RiskLevel.HIGH
            else -> RiskLevel.CRITICAL
        }
    }
}