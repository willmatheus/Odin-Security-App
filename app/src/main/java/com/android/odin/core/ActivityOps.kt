package com.android.odin.core

import android.content.ComponentName
import android.content.Intent
import com.android.odin.core.enums.ActivityComponents

data class ActivityOps(
    val startActivity: (Intent) -> Unit = {},
    val finish: () -> Unit = {},
    val resolveComponents: (ActivityComponents) -> ComponentName? = { null }
)
