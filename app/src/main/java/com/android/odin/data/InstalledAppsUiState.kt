package com.android.odin.data

import com.android.odin.core.ActivityOps

data class InstalledAppsUiState(
    val onLaunch: () -> Unit = {},
    val isLoading: Boolean = true,
    val installedAppsList: List<AppInfoUiState> = emptyList(),
    val startAppInfoActivity: (ActivityOps) -> Unit = {},
)