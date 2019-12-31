package com.flypika.pack.util.api

import android.content.Context
import com.flypika.pack.R
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

object ApiUtil {
    fun getApiErrorMessage(context: Context, throwable: Throwable): String = when (throwable) {
        is UnknownHostException -> context.getString(R.string.no_internet_error)
        is SocketTimeoutException, is TimeoutException -> context.getString(R.string.time_out_exception)
        else -> context.getString(R.string.unknown_error)
    }
}
