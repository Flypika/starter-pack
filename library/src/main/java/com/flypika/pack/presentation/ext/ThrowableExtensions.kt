package com.flypika.pack.presentation.ext

import android.util.Log
import com.crashlytics.android.Crashlytics

fun Throwable.log() {
    val message = Log.getStackTraceString(this)
    Log.e(TAG, message)

    Crashlytics.logException(this)
}
