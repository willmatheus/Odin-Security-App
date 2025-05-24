package com.android.odin.ui.theme.resources

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.android.odin.core.ActivityOps
import com.android.odin.data.AppInfoUiState
import com.android.odin.data.InstalledAppsUiState
import com.android.odin.ui.theme.YellowPrimary
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppInfoCard(app: AppInfoUiState, uiState: InstalledAppsUiState, activityOps: ActivityOps) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
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
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Status: ${app.status}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${app.permissions.size} permissions",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            TextButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                colors  = ButtonColors(
                    Color.Transparent,
                    YellowPrimary,
                    Color.Transparent,
                    YellowPrimary,
                ),
                shape = RectangleShape,
                onClick = { uiState.startAppInfoActivity(activityOps) }
            ) {
                Text(
                    text = "Check App",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}