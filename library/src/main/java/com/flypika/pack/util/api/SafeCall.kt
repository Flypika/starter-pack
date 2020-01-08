package com.flypika.pack.util.api

suspend inline fun <T> safeApiCall(crossinline block: suspend () -> T): ResultWrapper<T> = try {
    ResultWrapper.Success(block())
} catch (throwable: Throwable) {
    ResultWrapper.Failure(throwable)
}
