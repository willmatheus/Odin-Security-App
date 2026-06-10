package com.android.odin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.odin.core.ActivityOps
import com.android.odin.extensions.getActivityOps
import com.android.odin.screens.AppRiskDetailsScreen
import com.android.odin.screens.InstalledAppsScreen
import com.android.odin.ui.theme.OdinTheme
import com.android.odin.viewmodel.InstalledAppsViewModel.Companion.APP_PACKAGE_NAME_EXTRA
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppInfoActivity : ComponentActivity() {

    private val appsPermissionsViewModel: HiltInstalledAppsViewModel by viewModels()
    private val activityOps: ActivityOps by lazy { getActivityOps() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val packageName = intent.getStringExtra(APP_PACKAGE_NAME_EXTRA) ?: return
        setContent {
            OdinTheme {
                AppRiskDetailsScreen(
                    packageName,
                    uiState = appsPermissionsViewModel.uiState.collectAsStateWithLifecycle().value,
                    activityOps = activityOps
                )
            }
        }
    }
}