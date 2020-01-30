package com.flypika.pack.util.api

sealed class ResultWrapper<out T> {

    open class Success<T>(val value: T) : ResultWrapper<T>()

    data class Failure(val throwable: Throwable) : ResultWrapper<Nothing>()
}