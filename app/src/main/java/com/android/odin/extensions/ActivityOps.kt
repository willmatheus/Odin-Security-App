package com.android.odin.extensions

import android.content.ComponentName
import androidx.activity.ComponentActivity
import com.android.odin.AppInfoActivity
import com.android.odin.core.ActivityOps
import com.android.odin.core.enums.ActivityComponents

fun ComponentActivity.getActivityOps() = ActivityOps(
    startActivity = { startActivity(it) },
    finish = { finish() },
    resolveComponents = {
        when (it) {
            ActivityComponents.APP_INFO_ACTIVITY -> ComponentName(
                packageName,
                AppInfoActivity::class.java.name
            )
        }
    }
)