package com.flypika.pack.presentation.ext

import android.app.Activity
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

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