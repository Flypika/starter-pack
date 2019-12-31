package com.flypika.pack.ui.base.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flypika.pack.ui.livedata.manager.LiveEventManager
import com.flypika.pack.util.TAG
import com.flypika.pack.util.api.ApiUtil
import javax.inject.Inject

abstract class BaseViewModel<A : BaseViewAction> : ViewModel() {

    @Inject
    protected lateinit var context: Context

    val viewActionManager = LiveEventManager<Action<A>>()

    val loadingLiveData = MutableLiveData<Boolean>().apply {
        value = false
    }

    protected fun showLoading() {
        loadingLiveData.postValue(true)
    }

    protected fun hideLoading() {
        loadingLiveData.postValue(false)
    }

    fun logError(e: Throwable) {
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
        viewActionManager.postEvent(event = BaseViewAction::showUnknownError)
    }

    protected fun finishScreen() {
        viewActionManager.postEvent(event = BaseViewAction::finishScreen)
    }

    protected fun showMessage(msg: String) {
        viewActionManager.postEvent { showMessage(msg) }
    }

    protected fun showMessage(msg: Int) {
        viewActionManager.postEvent { showMessage(msg) }
    }
}