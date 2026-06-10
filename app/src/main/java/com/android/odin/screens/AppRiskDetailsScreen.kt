package com.android.odin.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.odin.PermissionRiskClassifier.AggravatingRule
import com.android.odin.PermissionRiskClassifier.PermissionContribution
import com.android.odin.PermissionRiskClassifier.RiskLevel
import com.android.odin.R
import com.android.odin.Severity
import com.android.odin.core.ActivityOps
import com.android.odin.data.AppRiskUiState
import com.android.odin.data.InstalledAppsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRiskDetailsScreen(
    appPackageName: String?,
    uiState: InstalledAppsUiState,
    activityOps: ActivityOps
) {
    LaunchedEffect(Unit) {
        uiState.onSelectApp(appPackageName!!)
    }

    val appRiskResult = uiState.selectedApp ?: AppRiskUiState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(appRiskResult.appName) },
                navigationIcon = {
                    IconButton(onClick = activityOps.finish) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                RiskSummaryCard(appRiskResult)
            }

            if (appRiskResult.aggravatingRules.isNotEmpty()) {
                item {
                    AggravatingRulesCard(appRiskResult.aggravatingRules)
                }
            }

            item {
                Text(
                    text = "Permissões analisadas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "As permissões abaixo indicam quais recursos sensíveis o aplicativo declarou ou recebeu acesso.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (appRiskResult.contributingPermissions.isEmpty()) {
                item {
                    EmptyPermissionsCard()
                }
            } else {
                items(appRiskResult.contributingPermissions) { permission ->
                    PermissionRiskCard(permission)
                }
            }
        }
    }
}

@Composable
private fun RiskSummaryCard(
    result: AppRiskUiState
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = result.riskLevel.color()
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = result.riskLevel.label(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = result.riskLevel.color()
                )
            }

            Text(
                text = result.riskLevel.description(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider()

            RiskInfoRow(
                label = "Índice baseado em permissões",
                value = "%.1f".format(result.baseRiskScore)
            )

            RiskInfoRow(
                label = "Fator de origem",
                value = "x%.1f".format(result.originFactor)
            )

            RiskInfoRow(
                label = "Índice final ajustado",
                value = "%.1f".format(result.finalRiskScore)
            )

            RiskInfoRow(
                label = "Classificação preliminar",
                value = result.preliminaryRiskLevel.label()
            )

            RiskInfoRow(
                label = "Origem de instalação",
                value = result.installOrigin.name
            )
        }
    }
}

@Composable
private fun AggravatingRulesCard(
    rules: List<AggravatingRule>
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Regras de agravamento acionadas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            rules.forEachIndexed { index, rule ->
                if (index > 0) {
                    HorizontalDivider()
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = rule.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = rule.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    RiskInfoRow(
                        label = "Classificação mínima",
                        value = rule.minimumRiskLevel.label()
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionRiskCard(
    permission: PermissionContribution
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = permission.permission.shortPermissionName(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = permission.permission,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                SeverityAssistChip(permission.severity)
            }

            Text(
                text = permission.description,
                style = MaterialTheme.typography.bodyMedium
            )

            HorizontalDivider()

            RiskInfoRow(
                label = "Peso da permissão",
                value = "%.1f".format(permission.weight)
            )

            RiskInfoRow(
                label = "Estado",
                value = if (permission.stateFactor == 1.0) {
                    stringResource(R.string.permission_granted)
                } else {
                    stringResource(R.string.permission_declared_not_granted)
                }
            )

            RiskInfoRow(
                label = "Contribuição no índice",
                value = "%.1f".format(permission.contribution)
            )
        }
    }
}

@Composable
private fun SeverityAssistChip(
    severity: Severity
) {
    AssistChip(
        onClick = {},
        label = {
            Text(text = severity.label())
        },
        colors = AssistChipDefaults.assistChipColors(
            labelColor = severity.color()
        )
    )
}

@Composable
private fun RiskInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun EmptyPermissionsCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Nenhuma permissão relevante foi identificada para o modelo de risco.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun RiskLevel.label(): String {
    return when (this) {
        RiskLevel.LOW -> "Risco baixo"
        RiskLevel.MODERATE -> "Risco moderado"
        RiskLevel.HIGH -> "Risco alto"
        RiskLevel.CRITICAL -> "Risco crítico"
    }
}

private fun RiskLevel.description(): String {
    return when (this) {
        RiskLevel.LOW ->
            "Aplicativo com poucas permissões sensíveis ou permissões de baixo impacto."

        RiskLevel.MODERATE ->
            "Aplicativo com algumas permissões relevantes, mas sem concentração elevada de permissões críticas."

        RiskLevel.HIGH ->
            "Aplicativo com múltiplas permissões sensíveis ou acesso a dados pessoais."

        RiskLevel.CRITICAL ->
            "Aplicativo com forte concentração de permissões críticas ou combinação com alto potencial de abuso."
    }
}

@Composable
private fun RiskLevel.color() = when (this) {
    RiskLevel.LOW -> MaterialTheme.colorScheme.primary
    RiskLevel.MODERATE -> MaterialTheme.colorScheme.tertiary
    RiskLevel.HIGH -> MaterialTheme.colorScheme.error
    RiskLevel.CRITICAL -> MaterialTheme.colorScheme.error
}

private fun Severity.label(): String {
    return when (this) {
        Severity.NONE -> "Sem risco"
        Severity.LOW -> "Baixa"
        Severity.MEDIUM -> "Média"
        Severity.HIGH -> "Alta"
        Severity.CRITICAL -> "Crítica"
        Severity.ASTRONOMICAL -> "Astronômica"
    }
}

@Composable
private fun Severity.color() = when (this) {
    Severity.NONE -> MaterialTheme.colorScheme.onSurfaceVariant
    Severity.LOW -> MaterialTheme.colorScheme.primary
    Severity.MEDIUM -> MaterialTheme.colorScheme.tertiary
    Severity.HIGH -> MaterialTheme.colorScheme.error
    Severity.CRITICAL -> MaterialTheme.colorScheme.error
    Severity.ASTRONOMICAL -> MaterialTheme.colorScheme.error
}

private fun String.shortPermissionName(): String {
    return substringAfterLast(".")
}
