package com.emotionstorage.ui.util

import android.content.Context
import android.util.TypedValue

fun dpToPixel(context: Context, dipValue: Float): Float {
    val metrics = context.getResources().getDisplayMetrics()
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
}
