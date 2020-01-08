package com.flypika.pack.ui.base.viewmodel

import com.flypika.pack.util.api.ResultWrapper
import com.flypika.pack.util.api.safeApiCall
import retrofit2.HttpException

abstract class AuthViewModel<A : ViewAction> : StarterViewModel<A>() {

    protected suspend inline fun <T> request(crossinline block: suspend () -> T): ResultWrapper<T> {
        val result: ResultWrapper<T> = safeApiCall(block)

        if (result is ResultWrapper.Failure &&
            result.throwable is HttpException &&
            result.throwable.code() == 401
        ) {
            onUnauthorized()
        }

        return result
    }

    protected abstract fun onUnauthorized()
}
