package com.flypika.pack.ext

import kotlinx.coroutines.CancellableContinuation
import kotlin.coroutines.resume

fun <T> CancellableContinuation<T>.safeResume(value: T) {
    try {
        if (isActive) {
            resume(value)
        }
    } catch (e: IllegalStateException) {
        e.log()
    }
}
