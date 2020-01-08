package com.flypika.pack.util.api

suspend inline fun <T> safeApiCall(crossinline block: suspend () -> T): ResultWrapper<T> = try {
    ResultWrapper.Success(block())
} catch (throwable: Throwable) {
    ResultWrapper.Failure(throwable)
}

suspend inline fun <T> noResultSafeApiCall(crossinline block: suspend () -> T): ResultWrapper<Unit> =
    safeApiCall(block).let {
        when (it) {
            is ResultWrapper.Success -> ResultWrapper.Success(Unit)
            is ResultWrapper.Failure -> it
        }
    }

