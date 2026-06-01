package com.android.odin.data

import com.android.odin.core.ActivityOps

data class InstalledAppsUiState(
    val onLaunch: () -> Unit = {},
    val isLoading: Boolean = true,
    val installedAppsList: List<AppRiskUiState> = emptyList(),
    val onSelectApp: (String) -> Unit = {},
    val selectedApp: AppRiskUiState? = null,
    val startAppInfoActivity: (ActivityOps, String) -> Unit = {_, _ -> },
)