package com.flypika.pack.presentation.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {

    val activityActionBehavior = SingleLiveEvent<VmAction>()

    private var isActive = true

    protected fun withActivity(block: (AppCompatActivity) -> Unit) {
        VmActionCompat(block).invokeAction()
    }

    private fun VmAction.invokeAction() {
        val isUiThread = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            android.os.Looper.getMainLooper().isCurrentThread
        } else {
            Thread.currentThread() === android.os.Looper.getMainLooper().thread
        }

        if (isUiThread) {
            activityActionBehavior.value = this
        } else {
            activityActionBehavior.postValue(this)
        }
    }

    // Do work in IO
    protected fun <P> doWork(doOnAsyncBlock: suspend CoroutineScope.() -> P) {
        doCoroutineWork(doOnAsyncBlock, viewModelScope, IO)
    }

    // Do work in Main
    // doWorkInMainThread {...}
    protected fun <P> doWorkInMainThread(doOnAsyncBlock: suspend CoroutineScope.() -> P) {
        doCoroutineWork(doOnAsyncBlock, viewModelScope, Main)
    }

    // Do work in IO repeately
    // doRepeatWork(1000) {...}
    // then we need to stop it calling stopRepeatWork()
    protected fun <P> doRepeatWork(delay: Long, doOnAsyncBlock: suspend CoroutineScope.() -> P) {
        isActive = true
        viewModelScope.launch {
            while (this@BaseViewModel.isActive) {
                withContext(IO) {
                    doOnAsyncBlock.invoke(this)
                }
                if (this@BaseViewModel.isActive) {
                    kotlinx.coroutines.delay(delay)
                }
            }
        }
    }

    protected fun stopRepeatWork() {
        isActive = false
    }

    override fun onCleared() {
        super.onCleared()
        isActive = false
    }

    private inline fun <P> doCoroutineWork(
        crossinline doOnAsyncBlock: suspend CoroutineScope.() -> P,
        coroutineScope: CoroutineScope,
        context: CoroutineContext
    ) {
        coroutineScope.launch {
            withContext(context) {
                doOnAsyncBlock.invoke(this)
            }
        }
    }

    fun navigateBack() {

    }

    fun navigateBackTo(destinationId: Int) {

    }

    fun navigateTo(direction: Int) {

    }

    fun navigateToRoot() {

    }
}