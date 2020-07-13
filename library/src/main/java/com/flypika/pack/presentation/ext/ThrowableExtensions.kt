package com.flypika.pack.presentation.ext

import android.util.Log
import timber.log.Timber

fun Throwable.log() {
    val message = Log.getStackTraceString(this)
    Timber.e("log: $message")
}