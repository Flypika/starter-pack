package com.flypika.pack.ui.base.viewmodel

import com.flypika.pack.util.api.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class AuthViewModel<A : ViewAction> : StarterViewModel<A>() {

    protected suspend inline fun <T> authorized(crossinline block: suspend () -> ResultWrapper<T>): ResultWrapper<T> {
        onAuthRequest()
        return block().apply { checkUnauthorized() }
    }

    protected suspend fun <T> ResultWrapper<T>.checkUnauthorized() {
        if (this is ResultWrapper.Failure &&
            this.throwable is HttpException &&
            this.throwable.code() == 401
        ) {
            withContext(Dispatchers.Main) { onUnauthorized() }
        }
    }

    protected abstract fun onUnauthorized()

    protected open suspend fun onAuthRequest() = Unit
}
