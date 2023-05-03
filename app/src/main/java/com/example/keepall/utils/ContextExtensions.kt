package com.example.keepall.utils

import android.content.Context
import kotlin.math.roundToInt

fun Context.getScreenWidth(): Int {
    return (this.resources.configuration.screenWidthDp * this.resources.displayMetrics.density).roundToInt()
}