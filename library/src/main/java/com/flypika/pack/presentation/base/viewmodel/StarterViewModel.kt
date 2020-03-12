package com.flypika.pack.presentation.base.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flypika.pack.R
import com.flypika.pack.data.utils.api.ResultWrapper
import com.flypika.pack.presentation.ext.log
import com.flypika.pack.presentation.livedata.manager.LiveEventManager
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class StarterViewModel<A : ViewAction> : ViewModel() {

    protected sealed class Error {
        object Internet : Error()
        object Timeout : Error()
        data class Http(val e: HttpException) : Error()
        data class Unknown(val throwable: Throwable) : Error()
    }

    val viewModelScope: CoroutineScope
        get() = (this as ViewModel).viewModelScope + CoroutineExceptionHandler { _, throwable ->
            Log.e(Thread.currentThread().name, Log.getStackTraceString(throwable))
            throw throwable
        }

    @Inject
    lateinit var context: Context

    val viewActionManager = LiveEventManager<Action<A>>()

    val loadingLiveData = MutableLiveData<Boolean>().apply {
        value = false
    }

    /**
     * Dagger 2 callback called when all fields are injected therefore initialized
     */
    open fun onInjected() {}

    fun showLoading() {
        loadingLiveData.postValue(true)
    }

    fun hideLoading() {
        loadingLiveData.postValue(false)
    }

    open fun handleServerError(throwable: Throwable) {
        viewActionManager.postEvent {
            throwable.toError()
                .toMessage()
                ?.let { showMessage(it) }
        }
        throwable.log()
    }

    protected open fun Error.toMessage(): String? = when (this) {
        is Error.Internet -> R.string.no_internet_error
        is Error.Timeout -> R.string.time_out_exception
        else -> R.string.unknown_error
    }.let { context.getString(it) }

    fun finishScreen() {
        viewActionManager.postEvent(event = ViewAction::finishScreen)
    }

    fun showMessage(msg: String) {
        viewActionManager.postEvent { showMessage(msg) }
    }

    fun showMessage(msg: Int) {
        viewActionManager.postEvent { showMessage(msg) }
    }

    inline fun <T> ResultWrapper<T>.doOnSuccess(block: (T) -> Unit) {
        when (this) {
            is ResultWrapper.Success -> block(this.value)
            is ResultWrapper.Failure -> handleServerError(this.throwable)
        }
    }

    inline fun launch(
        context: CoroutineContext = Dispatchers.IO,
        crossinline block: suspend () -> Unit
    ) {
        showLoading()
        viewModelScope.launch(context) {
            block()
            hideLoading()
        }
    }

    private fun Throwable.toError(): Error = when (this) {
        is UnknownHostException -> Error.Internet
        is SocketTimeoutException, is TimeoutException -> Error.Timeout
        is HttpException -> Error.Http(this)
        else -> Error.Unknown(this)
    }
}