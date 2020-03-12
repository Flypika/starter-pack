package com.flypika.pack.presentation.ext

import android.content.Context
import android.content.Intent
import android.net.Uri

fun String.openInBrowser(context: Context?) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(this))
    context?.startActivity(browserIntent)
}