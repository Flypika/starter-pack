package com.flypika.pack.presentation.base.viewmodel

import com.flypika.pack.data.network.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class AuthViewModel<A : ViewAction> : StarterViewModel<A>() {

    suspend inline fun <T> authorized(crossinline block: suspend () -> ResultWrapper<T>): ResultWrapper<T> {
        onAuthRequest()
        return block().apply {
            withContext(Dispatchers.Main) { checkUnauthorized() }
        }
    }

    fun <T> ResultWrapper<T>.checkUnauthorized() {
        if (this is ResultWrapper.Failure) {
            throwable.checkUnauthorized()
        }
    }

    protected fun Throwable.checkUnauthorized() {
        if (this is HttpException && code() == 401) {
            onUnauthorized()
        }
    }

    protected abstract fun onUnauthorized()

    open suspend fun onAuthRequest() = Unit
}
