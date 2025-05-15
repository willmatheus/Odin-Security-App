package com.android.odin.data

data class InstalledAppsUiState(
    val onLaunch: () -> Unit = {},
    val installedAppList: List<AppInfoUiState> = mutableListOf()
)