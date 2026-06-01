package com.android.odin.ui.theme.resources

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.odin.PermissionRiskClassifier.InstallOrigin
import com.android.odin.PermissionRiskClassifier.RiskLevel
import com.android.odin.R
import com.android.odin.core.ActivityOps
import com.android.odin.data.AppRiskUiState
import com.android.odin.data.InstalledAppsUiState
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppInfoCard(
    app: AppRiskUiState,
    uiState: InstalledAppsUiState,
    activityOps: ActivityOps,
    highlighted: Boolean
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (highlighted) {
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = rememberDrawablePainter(drawable = app.appIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .padding(end = 12.dp),
                    tint = Color.Unspecified
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = app.appName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = app.packageName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                RiskLevelChip(app.riskLevel)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OriginChip(app.installOrigin)

                AssistChip(
                    onClick = {},
                    label = {
                        Text("${app.contributingPermissions.size} permissões")
                    }
                )
            }

            Text(
                text = "Índice final: %.1f".format(app.finalRiskScore),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    uiState.startAppInfoActivity(activityOps, app.packageName)
                }
            ) {
                Text(text = stringResource(R.string.check_app))
            }
        }
    }
}

@Composable
private fun RiskLevelChip(
    riskLevel: RiskLevel
) {
    AssistChip(
        onClick = {},
        label = {
            Text(riskLevel.label())
        },
        colors = AssistChipDefaults.assistChipColors(
            labelColor = riskLevel.color()
        )
    )
}

@Composable
private fun OriginChip(
    installOrigin: InstallOrigin
) {
    AssistChip(
        onClick = {},
        label = {
            Text(installOrigin.label())
        },
        colors = AssistChipDefaults.assistChipColors(
            labelColor = installOrigin.color()
        )
    )
}

private fun RiskLevel.label(): String {
    return when (this) {
        RiskLevel.LOW -> "Baixo"
        RiskLevel.MODERATE -> "Moderado"
        RiskLevel.HIGH -> "Alto"
        RiskLevel.CRITICAL -> "Crítico"
    }
}

@Composable
private fun RiskLevel.color(): Color {
    return when (this) {
        RiskLevel.LOW -> MaterialTheme.colorScheme.primary
        RiskLevel.MODERATE -> MaterialTheme.colorScheme.tertiary
        RiskLevel.HIGH -> MaterialTheme.colorScheme.error
        RiskLevel.CRITICAL -> MaterialTheme.colorScheme.error
    }
}

private fun InstallOrigin.label(): String {
    return when (this) {
        InstallOrigin.GOOGLE_PLAY -> "Play Store"
        InstallOrigin.UNKNOWN -> "Origem desconhecida"
        InstallOrigin.EXTERNAL_APK -> "Fonte externa"
    }
}

@Composable
private fun InstallOrigin.color(): Color {
    return when (this) {
        InstallOrigin.GOOGLE_PLAY -> MaterialTheme.colorScheme.primary
        InstallOrigin.UNKNOWN -> MaterialTheme.colorScheme.tertiary
        InstallOrigin.EXTERNAL_APK -> MaterialTheme.colorScheme.error
    }
}