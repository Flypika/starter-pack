package com.flypika.pack.ext

import com.flypika.pack.util.api.ResultWrapper

inline fun <T> ResultWrapper<T>.handle(
    onSuccess: (T) -> Unit,
    onError: (Throwable) -> Unit
) {
    when (this) {
        is ResultWrapper.Success -> onSuccess(this.value)
        is ResultWrapper.Failure -> onError(this.throwable)
    }
}
