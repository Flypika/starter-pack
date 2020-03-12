package com.flypika.pack.presentation.ext

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

/**
 * Convert dp value to px
 */
fun Int.toPx(context: Context): Int {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return (this * (metrics.densityDpi.toFloat() / 160.0f)).toInt()
}

/**
 * Convert dp value to px
 */
fun Float.toPx(context: Context): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return (this * (metrics.densityDpi.toFloat() / 160.0f))
}


fun Context.screenDimension(): Point {
    val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}