package com.flypika.pack.presentation.ext

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

fun Activity.showAlert(
    message: String,
    title: String = "",
    positive: String = getString(android.R.string.ok),
    negative: String? = null,
    positiveCallback: (DialogInterface) -> Unit = { it.dismiss() },
    negativeCallback: (DialogInterface) -> Unit = { it.dismiss() }
) {
    val dialog = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positive) { dialog, which -> positiveCallback(dialog) }
    negative.let { dialog.setNegativeButton(negative) { dialog, which -> negativeCallback(dialog) } }
    dialog.show()
}

inline fun <reified A : AppCompatActivity> Context.startActivity(block: Intent.() -> Unit = {}) {
    val intent = Intent(this, A::class.java).apply(block)
    startActivity(intent)
}

fun Activity.changeStatusBarColor(color: Int, animate: Boolean = false) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    if (animate) {
        val startColor = window.statusBarColor
        val endColor = ContextCompat.getColor(this, color)
        ObjectAnimator.ofArgb(window, "statusBarColor", startColor, endColor).start()
    } else {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }
}

fun Activity.changeStatusBarTextColor(toDark: Boolean) {
    window.decorView.systemUiVisibility = if (toDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else 0
    } else 0
}