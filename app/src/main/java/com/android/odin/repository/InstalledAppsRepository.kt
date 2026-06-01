package com.android.odin.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstalledAppsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val packageManager: PackageManager
        get() = context.packageManager

    suspend fun getInstalledApps(): List<PackageInfo> = withContext(Dispatchers.IO) {
        val currentPackageName = context.packageName
        getInstalledPackages()
            .asSequence()
            .filterNot { it.packageName == currentPackageName }
            .filterNot { it.isSystemApp() }
            .filter { it.isLaunchable() }
            .filter { it.hasDeclaredPermissions() }
            .sortedBy { it.applicationInfo?.loadLabel(packageManager).toString().lowercase() }
            .toList()
    }

    private fun getInstalledPackages(): List<PackageInfo> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getInstalledPackages(
                PackageManager.PackageInfoFlags.of(
                    PackageManager.GET_PERMISSIONS.toLong()
                )
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        }
    }

    private fun PackageInfo.isSystemApp(): Boolean {
        val flags = applicationInfo?.flags ?: return false

        return flags and ApplicationInfo.FLAG_SYSTEM != 0 ||
                flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
    }

    private fun PackageInfo.isLaunchable(): Boolean {
        return packageManager.getLaunchIntentForPackage(packageName) != null
    }

    private fun PackageInfo.hasDeclaredPermissions(): Boolean {
        return !requestedPermissions.isNullOrEmpty()
    }
}
