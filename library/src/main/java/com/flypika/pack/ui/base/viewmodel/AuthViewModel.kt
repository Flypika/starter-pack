package com.flypika.pack.ui.base.viewmodel

import com.flypika.pack.util.api.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class AuthViewModel<A : ViewAction> : StarterViewModel<A>() {

    suspend inline fun <T> authorized(crossinline block: suspend () -> ResultWrapper<T>): ResultWrapper<T> {
        onAuthRequest()
        return block().apply { checkUnauthorized() }
    }

    suspend fun <T> ResultWrapper<T>.checkUnauthorized() {
        if (this is ResultWrapper.Failure) {
            throwable.checkUnauthorized()
        }
    }

    protected suspend fun Throwable.checkUnauthorized() {
        if (this is HttpException && code() == 401) {
            withContext(Dispatchers.Main) { onUnauthorized() }
        }
    }

    protected abstract fun onUnauthorized()

    open suspend fun onAuthRequest() = Unit
}
