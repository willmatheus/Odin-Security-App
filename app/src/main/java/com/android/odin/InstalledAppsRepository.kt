package com.android.odin

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstalledAppsRepository @Inject constructor (
    @ApplicationContext private val context: Context
) {

    fun getPackageManager(): PackageManager = context.packageManager

    fun getInstalledApps() : MutableList<PackageInfo> {
        val pm = getPackageManager()
        val packages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        return packages
    }

}
