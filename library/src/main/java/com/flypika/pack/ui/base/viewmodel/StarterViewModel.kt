package com.flypika.pack.ui.base.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crashlytics.android.Crashlytics
import com.flypika.pack.ui.livedata.manager.LiveEventManager
import com.flypika.pack.util.TAG
import com.flypika.pack.util.api.ApiUtil
import com.flypika.pack.util.api.ResultWrapper
import kotlinx.coroutines.*
import javax.inject.Inject

abstract class StarterViewModel<A : ViewAction> : ViewModel() {

    val viewModelScope: CoroutineScope get() = (this as ViewModel).viewModelScope + CoroutineExceptionHandler { _, throwable ->
        Log.e(Thread.currentThread().name, Log.getStackTraceString(throwable))
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

    protected fun handleServerError(throwable: Throwable) {
        hideLoading()
        viewActionManager.postEvent {
            val msg = ApiUtil.getApiErrorMessage(context, throwable)
            showMessage(msg)
        }
        logError(throwable)
    }

    protected fun showUnknownError() {
        viewActionManager.postEvent(event = ViewAction::showUnknownError)
    }

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

    protected inline fun launch(crossinline block: suspend () -> Unit) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            block()
            hideLoading()
        }
    }
}