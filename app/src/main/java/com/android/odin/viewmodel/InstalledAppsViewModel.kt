package com.android.odin.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.odin.PermissionClassifierWrapper
import com.android.odin.repository.InstalledAppsRepository
import com.android.odin.core.ActivityOps
import com.android.odin.core.enums.ActivityComponents
import com.android.odin.data.AppInfoUiState
import com.android.odin.data.InstalledAppsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class InstalledAppsViewModel(
    private val installedAppsRepository: InstalledAppsRepository
) : ViewModel() {

    private val permissionClassifierWrapper = PermissionClassifierWrapper

    private val _uiState = MutableStateFlow(
        InstalledAppsUiState(
            onLaunch = ::loadAllInstalledApps,
            startAppInfoActivity = ::startAppInfoActivity
        )
    )

    val uiState = _uiState.asStateFlow()

    private fun loadAllInstalledApps() {
        viewModelScope.launch {
            _uiState.update { update ->
                update.copy(
                    installedAppsList = getInstalledApps(),
                    isLoading = false
                )
            }
        }
    }

    private fun startAppInfoActivity(activityOps: ActivityOps) {
        val intent = Intent().apply {
            component = activityOps.resolveComponents(ActivityComponents.APP_INFO_ACTIVITY)
        }
        activityOps.startActivity(intent)
    }

    private suspend fun getInstalledApps() : List<AppInfoUiState> {
        val pm = installedAppsRepository.getPackageManager()
        val installedApps = installedAppsRepository.getInstalledApps()

        return installedApps.map {
            val name = it.applicationInfo?.loadLabel(pm).toString()
            val permissions = it.requestedPermissions?.toList() ?: emptyList()
            val status = permissionClassifierWrapper.classifyAppThreatStatus(it, pm)
            AppInfoUiState(name, it.packageName, permissions, it.applicationInfo?.loadIcon(pm), status)
        }
    }
}