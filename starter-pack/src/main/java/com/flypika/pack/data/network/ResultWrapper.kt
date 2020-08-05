package com.flypika.pack.data.network

sealed class ResultWrapper<out T> {

    open class Success<T>(val value: T) : ResultWrapper<T>()

    data class Failure(val throwable: Throwable) : ResultWrapper<Nothing>()
}