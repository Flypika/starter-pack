package com.flypika.pack.presentation.base.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flypika.pack.R
import com.flypika.pack.data.network.ResultWrapper
import com.flypika.pack.presentation.ext.log
import com.flypika.pack.presentation.livedata.manager.LiveEventManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

abstract class StarterViewModel<A : ViewAction>(
    val context: Context
) : ViewModel() {

    val viewModelScope: CoroutineScope
        get() = (this as ViewModel).viewModelScope + CoroutineExceptionHandler { _, throwable ->
            Log.e(Thread.currentThread().name, Log.getStackTraceString(throwable))
            throw throwable
        }
    val viewActionManager = LiveEventManager<Action<A>>()
    val loadingLiveData = MutableLiveData<Boolean>().apply {
        value = false
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

    private fun Throwable.toError(): Error = when (this) {
        is UnknownHostException -> Error.Internet
        is SocketTimeoutException, is TimeoutException -> Error.Timeout
        is HttpException -> Error.Http(this)
        else -> Error.Unknown(this)
    }

    protected sealed class Error {
        object Internet : Error()
        object Timeout : Error()
        data class Http(val e: HttpException) : Error()
        data class Unknown(val throwable: Throwable) : Error()
    }
}