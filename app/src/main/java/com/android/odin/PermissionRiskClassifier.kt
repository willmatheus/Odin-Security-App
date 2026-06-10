package com.android.odin

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import com.android.odin.data.AppRiskUiState

object PermissionRiskClassifier {

    private const val PLAY_STORE_PACKAGE = "com.android.vending"
    private const val INSTALL_PACKAGES_PERMISSION = "android.permission.INSTALL_PACKAGES"
    private const val BIND_ACCESSIBILITY_SERVICE_PERMISSION =
        "android.permission.BIND_ACCESSIBILITY_SERVICE"
    private const val SYSTEM_ALERT_WINDOW_PERMISSION = "android.permission.SYSTEM_ALERT_WINDOW"
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

    data class AggravatingRule(
        val title: String,
        val description: String,
        val minimumRiskLevel: RiskLevel
    )

    private data class DeclaredPermission(
        val name: String,
        val granted: Boolean
    )

    fun classifyApp(
        packageInfo: PackageInfo,
        packageManager: PackageManager
    ): AppRiskUiState {
        val packageName = packageInfo.packageName

        val appName = try {
            packageInfo.applicationInfo?.loadLabel(packageManager)?.toString()
        } catch (exception: Exception) {
            Log.e(TAG, "Erro ao carregar nome do app: $packageName", exception)
            null
        } ?: packageName

        val appIcon = try {
            packageInfo.applicationInfo?.loadIcon(packageManager)
        } catch (exception: Exception) {
            Log.e(TAG, "Erro ao carregar ícone do app: $packageName", exception)
            null
        }

        val permissionFlags = packageInfo.requestedPermissionsFlags ?: IntArray(0)
        val permissions = extractDeclaredPermissions(packageInfo, permissionFlags)

        val installOrigin = getInstallOrigin(packageManager, packageName)

        val contributions = permissions.mapNotNull { permission ->
            val risk = permissionRiskMap[permission.name] ?: return@mapNotNull null

            val stateFactor = if (permission.granted) {
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
        val preliminaryRiskLevel = classifyPreliminaryRiskLevel(finalRiskScore)
        val aggravatingRules = findAggravatingRules(
            contributions = contributions,
            installOrigin = installOrigin,
            finalRiskScore = finalRiskScore
        )
        val riskLevel = maxRiskLevel(
            preliminaryRiskLevel,
            aggravatingRules.maxOfOrNull { it.minimumRiskLevel } ?: preliminaryRiskLevel
        )

        return AppRiskUiState(
            packageName = packageName,
            appName = appName,
            appIcon = appIcon,
            baseRiskScore = baseRiskScore,
            originFactor = originFactor,
            finalRiskScore = finalRiskScore,
            preliminaryRiskLevel = preliminaryRiskLevel,
            riskLevel = riskLevel,
            installOrigin = installOrigin,
            contributingPermissions = contributions,
            aggravatingRules = aggravatingRules
        )
    }

    private fun extractDeclaredPermissions(
        packageInfo: PackageInfo,
        permissionFlags: IntArray
    ): List<DeclaredPermission> {
        val permissions = linkedMapOf<String, Boolean>()
        val requestedPermissions = packageInfo.requestedPermissions ?: emptyArray()

        requestedPermissions.forEachIndexed { index, permission ->
            permissions[permission] = isPermissionGranted(permissionFlags, index)
        }

        val declaresAccessibilityService = packageInfo.services
            ?.any { it.permission == BIND_ACCESSIBILITY_SERVICE_PERMISSION }
            ?: false

        if (declaresAccessibilityService) {
            permissions[BIND_ACCESSIBILITY_SERVICE_PERMISSION] =
                permissions[BIND_ACCESSIBILITY_SERVICE_PERMISSION] == true
        }

        return permissions.map { (permission, granted) ->
            DeclaredPermission(permission, granted)
        }
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

    private fun classifyPreliminaryRiskLevel(finalRiskScore: Double): RiskLevel {
        return when {
            finalRiskScore <= 14.0 -> RiskLevel.LOW
            finalRiskScore <= 34.0 -> RiskLevel.MODERATE
            finalRiskScore <= 59.0 -> RiskLevel.HIGH
            else -> RiskLevel.CRITICAL
        }
    }

    private fun findAggravatingRules(
        contributions: List<PermissionContribution>,
        installOrigin: InstallOrigin,
        finalRiskScore: Double
    ): List<AggravatingRule> {
        val permissions = contributions.map { it.permission }.toSet()
        val rules = mutableListOf<AggravatingRule>()

        if (INSTALL_PACKAGES_PERMISSION in permissions) {
            rules += AggravatingRule(
                title = "INSTALL_PACKAGES presente",
                description = "Permite instalar pacotes e recebe classificacao final critica.",
                minimumRiskLevel = RiskLevel.CRITICAL
            )
        }

        if (
            installOrigin == InstallOrigin.EXTERNAL_APK &&
            BIND_ACCESSIBILITY_SERVICE_PERMISSION in permissions
        ) {
            rules += AggravatingRule(
                title = "Fonte externa com acessibilidade",
                description = "Apps externos com servico de acessibilidade recebem classificacao final critica.",
                minimumRiskLevel = RiskLevel.CRITICAL
            )
        }

        if (
            BIND_ACCESSIBILITY_SERVICE_PERMISSION in permissions &&
            SYSTEM_ALERT_WINDOW_PERMISSION in permissions
        ) {
            rules += AggravatingRule(
                title = "Acessibilidade combinada com sobreposicao",
                description = "A combinacao de acessibilidade e janela sobre outros apps recebe classificacao final critica.",
                minimumRiskLevel = RiskLevel.CRITICAL
            )
        }

        if (
            android.Manifest.permission.READ_SMS in permissions &&
            android.Manifest.permission.SEND_SMS in permissions
        ) {
            rules += AggravatingRule(
                title = "Leitura e envio de SMS",
                description = "A combinacao de leitura e envio de SMS eleva a classificacao final para, no minimo, risco alto.",
                minimumRiskLevel = RiskLevel.HIGH
            )
        }

        if (
            installOrigin == InstallOrigin.EXTERNAL_APK &&
            finalRiskScore >= 45.0
        ) {
            rules += AggravatingRule(
                title = "Fonte externa com indice elevado",
                description = "Apps externos com indice final igual ou superior a 45 recebem classificacao final critica.",
                minimumRiskLevel = RiskLevel.CRITICAL
            )
        }

        if (
            installOrigin == InstallOrigin.UNKNOWN &&
            finalRiskScore >= 50.0
        ) {
            rules += AggravatingRule(
                title = "Origem desconhecida com indice elevado",
                description = "Apps de origem desconhecida com indice final igual ou superior a 50 recebem classificacao final critica.",
                minimumRiskLevel = RiskLevel.CRITICAL
            )
        }

        return rules
    }

    private fun maxRiskLevel(
        first: RiskLevel,
        second: RiskLevel
    ): RiskLevel {
        return if (first.ordinal >= second.ordinal) first else second
    }
}
