package com.android.odin

import com.android.odin.repository.InstalledAppsRepository
import com.android.odin.viewmodel.InstalledAppsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HiltInstalledAppsViewModel @Inject constructor(
    installedAppsRepository: InstalledAppsRepository
) : InstalledAppsViewModel(installedAppsRepository)