package com.android.odin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.odin.data.AppInfoUiState
import com.android.odin.data.InstalledAppsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class InstalledAppsViewModel(
    private val installedAppsRepository: InstalledAppsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        InstalledAppsUiState(
            onLaunch = ::loadAllInstalledApps,
            installedAppList = getInstalledApps()
        )
    )

    val uiState = _uiState.asStateFlow()

    private fun loadAllInstalledApps() {
        viewModelScope.launch {
            _uiState.update { update ->
                update.copy(
                    installedAppList = getInstalledApps()
                )
            }
        }
    }

    private fun getInstalledApps() : List<AppInfoUiState> {
        val pm = installedAppsRepository.getPackageManager()

        val installedApps = installedAppsRepository.getInstalledApps().filter {
            val isUserApp = (it.applicationInfo?.flags?.and(android.content.pm.ApplicationInfo.FLAG_SYSTEM)) == 0
            val hasUi = pm.getLaunchIntentForPackage(it.packageName) != null
            isUserApp && hasUi
        }

        return installedApps.map {
            val name = it.applicationInfo?.loadLabel(pm).toString()
            val permissions = it.requestedPermissions?.toList() ?: emptyList()
            AppInfoUiState(name, it.packageName, permissions, it.applicationInfo?.loadIcon(pm))
        }
    }
}