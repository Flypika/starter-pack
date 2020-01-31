package com.flypika.pack.ui.base.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crashlytics.android.Crashlytics
import com.flypika.pack.ui.livedata.manager.LiveEventManager
import com.flypika.pack.util.TAG
import com.flypika.pack.util.api.ResultWrapper
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

abstract class StarterViewModel<A : ViewAction> : ViewModel() {

    protected enum class Error {
        INTERNET, TIMEOUT, UNKNOWN
    }

    val viewModelScope: CoroutineScope
        get() = (this as ViewModel).viewModelScope + CoroutineExceptionHandler { _, throwable ->
            Log.e(Thread.currentThread().name, Log.getStackTraceString(throwable))
            throw throwable
        }

    @Inject
    protected lateinit var context: Context

    val viewActionManager = LiveEventManager<Action<A>>()

    val loadingLiveData = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun showLoading() {
        loadingLiveData.postValue(true)
    }

    fun hideLoading() {
        loadingLiveData.postValue(false)
    }

    fun logError(e: Throwable) {
        Crashlytics.logException(e)

        val message = Log.getStackTraceString(e)
        Log.e(TAG, message)
    }

    protected open fun handleServerError(throwable: Throwable) {
        viewActionManager.postEvent {
            showMessage(throwable.toError().toMessage())
        }
        logError(throwable)
    }

    protected abstract fun Error.toMessage(): String

    protected fun finishScreen() {
        viewActionManager.postEvent(event = ViewAction::finishScreen)
    }

    protected fun showMessage(msg: String) {
        viewActionManager.postEvent { showMessage(msg) }
    }

    protected fun showMessage(msg: Int) {
        viewActionManager.postEvent { showMessage(msg) }
    }

    protected inline fun <T> ResultWrapper<T>.doOnSuccess(block: (T) -> Unit) {
        when (this) {
            is ResultWrapper.Success -> block(this.value)
            is ResultWrapper.Failure -> handleServerError(this.throwable)
        }
    }

    protected inline fun <T> ResultWrapper<T>.handle(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        when (this) {
            is ResultWrapper.Success -> onSuccess(this.value)
            is ResultWrapper.Failure -> onError(this.throwable)
        }
    }


    protected inline fun launch(crossinline block: suspend () -> Unit) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            block()
            hideLoading()
        }
    }

    private fun Throwable.toError(): Error = when (this) {
        is UnknownHostException -> Error.INTERNET
        is SocketTimeoutException, is TimeoutException -> Error.TIMEOUT
        else -> Error.UNKNOWN
    }
}