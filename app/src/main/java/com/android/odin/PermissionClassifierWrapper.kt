package com.android.odin

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import com.android.odin.core.enums.ThreatStatus

object PermissionClassifierWrapper {

    fun classifyAppThreatStatus(packageInfo: PackageInfo, packageManager: PackageManager) : ThreatStatus {
        val permissions = packageInfo.requestedPermissions ?: return ThreatStatus.SAFE
        val permissionRiskList = getPermissionsRiskList(permissions.toList())

        return classify(
            permissionRiskList,
            isInstalledFromPlayStore(packageManager, packageInfo.packageName)
        )
    }

    private fun classify(permissionRiskList: List<PermissionRisk>, isInstalledFromPlayStore: Boolean) : ThreatStatus {
        if (permissionRiskList.contains(PermissionRisk.ASTRONOMICAL) && !isInstalledFromPlayStore) {
            return ThreatStatus.DANGEROUS
        }
        return ThreatStatus.SAFE
    }

    private fun isInstalledFromPlayStore(packageManager: PackageManager, packageName: String) : Boolean {
        val installerPackageName = packageManager.getInstallSourceInfo(packageName).initiatingPackageName
        Log.d("isInstalledFromPlayStore", "package name: $packageName | installerPackageName: $installerPackageName")
        return installerPackageName == "com.android.vending"
    }

    private fun getPermissionsRiskList(permissionList: List<String>) : List<PermissionRisk> =
        permissionList.map { categorizePermission(it) }

    private fun categorizePermission(permission: String) : PermissionRisk =
        PERMISSION_RISK_MAP[permission] ?: PermissionRisk.UNKNOWN

}