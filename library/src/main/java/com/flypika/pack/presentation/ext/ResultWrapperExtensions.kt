package com.flypika.pack.presentation.ext

import com.flypika.pack.data.utils.api.ResultWrapper

inline fun <T> ResultWrapper<T>.handle(
    onSuccess: (T) -> Unit,
    onError: (Throwable) -> Unit
) {
    when (this) {
        is ResultWrapper.Success -> onSuccess(this.value)
        is ResultWrapper.Failure -> onError(this.throwable)
    }
}
