package com.flypika.pack.presentation.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> MutableLiveData<T>.observe(lifecycleOwner: LifecycleOwner, callback: (T) -> Unit) {
    observe(lifecycleOwner, Observer { callback(it ?: return@Observer) })
}

fun <T> MutableLiveData<T>.def(defaultState: T): MutableLiveData<T> {
    this.value = defaultState
    return this
}

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, callback: (T) -> Unit) {
    observe(lifecycleOwner, Observer { callback(it ?: return@Observer) })
}