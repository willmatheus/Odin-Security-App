package com.android.odin.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.odin.data.InstalledAppsUiState
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun InstalledAppsScreen(
    uiState: InstalledAppsUiState
) {
    LaunchedEffect(Unit) {
        uiState.onLaunch
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.installedAppList) { app ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = rememberDrawablePainter(drawable = app.appIcon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 12.dp),
                            tint = Color.Unspecified,
                        )
                        Column {
                            Text(
                                text = app.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Status: ${app.status}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "${app.permissions.size} permissions",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}