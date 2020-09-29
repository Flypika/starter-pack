package com.flypika.pack.presentation.ext

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
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

fun Context.sendEmail(
    address: Array<String>,
    subject: String = ""
) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, address)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(Intent.createChooser(intent, null))
}

fun Context.shareText(text: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(Intent.createChooser(sendIntent, null))
}

fun Context.openInBrowser(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(Intent.createChooser(browserIntent, null))
}

fun Context.callNumber(phone: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$phone")).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(Intent.createChooser(intent, null))
}

fun Context.openAppMarketPage() {
    try {
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        startActivity(intent)
    } catch (exception: ActivityNotFoundException) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(Intent.createChooser(intent, null))
    }
}