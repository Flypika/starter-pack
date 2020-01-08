package com.flypika.pack.ui.base.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

abstract class AuthViewModel<A : ViewAction> : StarterViewModel<A>() {

    protected inline fun request(crossinline block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    onUnauthorized()
                } else {
                    throw e
                }
            }
        }
    }

    protected abstract fun onUnauthorized()
}
