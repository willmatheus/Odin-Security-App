package com.android.odin.data

import android.graphics.drawable.Drawable
import com.android.odin.PermissionRiskClassifier.InstallOrigin
import com.android.odin.PermissionRiskClassifier.PermissionContribution
import com.android.odin.PermissionRiskClassifier.RiskLevel

data class AppRiskUiState(
    val packageName: String = "",
    val appName: String = "",
    val appIcon: Drawable? = null,
    val baseRiskScore: Double = 0.0,
    val finalRiskScore: Double = 0.0,
    val riskLevel: RiskLevel = RiskLevel.LOW,
    val installOrigin: InstallOrigin = InstallOrigin.UNKNOWN,
    val contributingPermissions: List<PermissionContribution> = listOf()
)
