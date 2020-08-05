package com.flypika.pack.presentation.ext

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.view.WindowManager
import androidx.core.content.ContextCompat
import kotlin.reflect.KClass

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result

}

fun Context.getColorCompat(res: Int) = ContextCompat.getColor(this, res)

fun Context.getDrawableCompat(res: Int) = ContextCompat.getDrawable(this, res)

inline fun <reified T : Any> Any?.cast(cl: KClass<T>): T? {
    if (this is T) {
        return this
    }

    return null
}

fun Intent.asNewTask(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    return this
}

fun Context.getScreenSize(): Point {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}