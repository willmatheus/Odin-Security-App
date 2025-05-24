package com.android.odin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.odin.core.ActivityOps
import com.android.odin.extensions.getActivityOps
import com.android.odin.screens.InstalledAppsScreen
import com.android.odin.ui.theme.OdinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppInfoActivity : ComponentActivity() {

    private val appsPermissionsViewModel: HiltInstalledAppsViewModel by viewModels()
    private val activityOps: ActivityOps by lazy { getActivityOps() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OdinTheme {
                InstalledAppsScreen(appsPermissionsViewModel.uiState.collectAsStateWithLifecycle().value, activityOps)
            }
        }
    }
}