package com.flypika.pack.data.network

import retrofit2.HttpException
import retrofit2.Response

suspend inline fun <T> safeApiCall(crossinline block: suspend () -> T): ResultWrapper<T> = try {
    ResultWrapper.Success(block())
} catch (throwable: Throwable) {
    ResultWrapper.Failure(throwable)
}

object VoidSuccess : ResultWrapper.Success<Unit>(Unit)

suspend inline fun <T> voidApiCall(crossinline block: suspend () -> Response<T>): ResultWrapper<Unit> {
    val response = try {
        block()
    } catch (throwable: Throwable) {
        return ResultWrapper.Failure(throwable)
    }
    return if (response.isSuccessful) VoidSuccess
    else ResultWrapper.Failure(HttpException(response))
}