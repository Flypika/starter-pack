package com.flypika.pack.presentation.ext

import androidx.lifecycle.*
import com.flypika.pack.presentation.base.SingleLiveEvent

fun <T> MutableLiveData<T>.def(defaultState: T): MutableLiveData<T> {
    this.value = defaultState
    return this
}

inline fun <reified T> SingleLiveEvent<T>.observe(
    lifecycleOwner: LifecycleOwner,
    crossinline callback: (T) -> Unit
) = (this as LiveData<T>).observe(lifecycleOwner, callback)

inline fun <reified T> MutableLiveData<T>.observe(
    lifecycleOwner: LifecycleOwner,
    crossinline callback: (T) -> Unit
) = (this as LiveData<T>).observe(lifecycleOwner, callback)

inline fun <reified T> MediatorLiveData<T>.observe(
    lifecycleOwner: LifecycleOwner,
    crossinline callback: (T) -> Unit
) = (this as LiveData<T>).observe(lifecycleOwner, callback)

inline fun <reified T> LiveData<T>.observe(
    lifecycleOwner: LifecycleOwner,
    crossinline callback: (T) -> Unit
) {
    observe(lifecycleOwner, Observer {
        if (null is T) {
            callback(it)
        } else {
            callback(it ?: return@Observer)
        }
    })
}
