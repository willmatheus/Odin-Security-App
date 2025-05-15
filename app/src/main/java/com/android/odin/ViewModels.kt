package com.android.odin

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HiltInstalledAppsViewModel @Inject constructor(
    installedAppsRepository: InstalledAppsRepository
) : InstalledAppsViewModel(installedAppsRepository)