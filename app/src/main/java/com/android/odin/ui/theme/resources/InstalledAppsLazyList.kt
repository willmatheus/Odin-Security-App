package com.android.odin.ui.theme.resources

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.odin.PermissionRiskClassifier.InstallOrigin
import com.android.odin.core.ActivityOps
import com.android.odin.data.AppRiskUiState
import com.android.odin.data.InstalledAppsUiState

@Composable
fun InstalledAppsLazyList(
    uiState: InstalledAppsUiState,
    paddingValues: PaddingValues,
    activityOps: ActivityOps
) {
    val externalApps = uiState.installedAppsList
        .filter { it.installOrigin != InstallOrigin.GOOGLE_PLAY }
        .sortedByDescending { it.finalRiskScore }

    val playStoreApps = uiState.installedAppsList
        .filter { it.installOrigin == InstallOrigin.GOOGLE_PLAY }
        .sortedByDescending { it.finalRiskScore }

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (externalApps.isNotEmpty()) {
            item {
                AppsSectionHeader(
                    title = "Apps de outras fontes",
                    subtitle = "${externalApps.size} app(s) instalados fora da Play Store"
                )
            }

            items(
                items = externalApps,
                key = { it.packageName }
            ) { app ->
                AppInfoCard(
                    app = app,
                    uiState = uiState,
                    activityOps = activityOps,
                    highlighted = true
                )
            }
        }

        if (playStoreApps.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                AppsSectionHeader(
                    title = "Apps da Play Store",
                    subtitle = "${playStoreApps.size} app(s) com origem oficial"
                )
            }

            items(
                items = playStoreApps,
                key = { it.packageName }
            ) { app ->
                AppInfoCard(
                    app = app,
                    uiState = uiState,
                    activityOps = activityOps,
                    highlighted = false
                )
            }
        }
    }
}

@Composable
private fun AppsSectionHeader(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}