package com.android.odin.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.odin.core.ActivityOps
import com.android.odin.data.InstalledAppsUiState
import com.android.odin.ui.theme.resources.InstalledAppsLazyList

@Composable
fun InstalledAppsScreen(
    uiState: InstalledAppsUiState,
    activityOps: ActivityOps
) {
    LaunchedEffect(Unit) {
        uiState.onLaunch()
    }

    Scaffold { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Aplicativos analisados",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Apps instalados fora da Play Store aparecem primeiro por apresentarem maior incerteza de origem.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    InstalledAppsLazyList(
                        uiState = uiState,
                        paddingValues = PaddingValues(bottom = 16.dp),
                        activityOps = activityOps
                    )
                }
            }
        }
    }
}