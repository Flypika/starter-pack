package com.flypika.pack.presentation.ext

import java.lang.ref.WeakReference

fun <T> T.weak() = WeakReference(this)