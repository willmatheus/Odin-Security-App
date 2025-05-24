package com.android.odin.ui.theme.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.odin.core.ActivityOps
import com.android.odin.data.InstalledAppsUiState

@Composable
fun InstalledAppsLazyList(uiState: InstalledAppsUiState, paddingValues: PaddingValues, activityOps: ActivityOps) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uiState.installedAppsList) { app ->
            AppInfoCard(app, uiState, activityOps)
        }
    }
}