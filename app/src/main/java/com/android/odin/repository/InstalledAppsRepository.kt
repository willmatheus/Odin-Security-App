package com.android.odin.repository

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstalledAppsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getPackageManager(): PackageManager = context.packageManager

    suspend fun getInstalledApps(): MutableList<PackageInfo> = withContext(Dispatchers.IO) {
        val currentPackageName = context.packageName
        getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS)
            .filter {
                it.applicationInfo?.flags?.and(android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0 &&
                getPackageManager().getLaunchIntentForPackage(it.packageName) != null &&
                it.packageName != currentPackageName
            }
            .toMutableList()
    }
}

