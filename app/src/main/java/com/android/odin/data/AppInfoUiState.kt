package com.android.odin.data

import android.graphics.drawable.Drawable
import com.android.odin.core.enums.ThreatStatus

data class AppInfoUiState(
    val name: String,
    val packageName: String,
    val permissions: List<String>,
    val appIcon: Drawable?,
    val status: ThreatStatus
)
