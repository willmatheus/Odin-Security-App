package com.android.odin.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.odin.PermissionRiskClassifier
import com.android.odin.core.ActivityOps
import com.android.odin.core.enums.ActivityComponents
import com.android.odin.data.AppRiskUiState
import com.android.odin.data.InstalledAppsUiState
import com.android.odin.repository.InstalledAppsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class InstalledAppsViewModel(
    private val installedAppsRepository: InstalledAppsRepository
) : ViewModel() {

    private val permissionRiskClassifier = PermissionRiskClassifier

    private val _uiState = MutableStateFlow(
        InstalledAppsUiState(
            onLaunch = ::loadAllInstalledApps,
            onSelectApp = ::loadSelectedApp,
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

    private fun loadSelectedApp(packageName: String) {
        viewModelScope.launch {
            _uiState.update { update ->
                update.copy(
                    selectedApp = getSelectedApp(packageName)
                )
            }
        }
    }

    private fun startAppInfoActivity(activityOps: ActivityOps, packageName: String) {
        val intent = Intent().apply {
            component = activityOps.resolveComponents(ActivityComponents.APP_INFO_ACTIVITY)
            putExtra(APP_PACKAGE_NAME_EXTRA, packageName)
        }
        activityOps.startActivity(intent)
    }

    private suspend fun getSelectedApp(packageName: String) : AppRiskUiState {
        val pm = installedAppsRepository.packageManager
        val apps = installedAppsRepository.getInstalledApps()
        val isApp = apps.lastOrNull { it.applicationInfo?.packageName == packageName }

        return if (isApp != null) {
            permissionRiskClassifier.classifyApp(isApp, pm)
        } else {
            AppRiskUiState()
        }
    }

    private suspend fun getInstalledApps() : List<AppRiskUiState> {
        val pm = installedAppsRepository.packageManager
        val installedApps = installedAppsRepository.getInstalledApps()
        return installedApps.map { permissionRiskClassifier.classifyApp(it, pm) }
    }

    companion object {
        const val APP_PACKAGE_NAME_EXTRA = "app_package_name"
    }
}