package com.android.odin.data

import android.graphics.drawable.Drawable
import com.android.odin.ThreatStatus

data class AppInfoUiState(
    val name: String,
    val packageName: String,
    val permissions: List<String>,
    val appIcon: Drawable?,
    val status: ThreatStatus = ThreatStatus.SAFE
)
