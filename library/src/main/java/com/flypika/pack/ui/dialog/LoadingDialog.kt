package com.flypika.pack.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.ViewGroup
import android.widget.ProgressBar
import com.flypika.pack.R

class LoadingDialog private constructor(
    context: Context,
    theme: Int
) : Dialog(context, theme) {

    companion object {

        @JvmOverloads
        fun show(
            context: Context,
            theme: Int = R.style.LoadingDialog_Dim,
            cancelable: Boolean = false,
            cancelListener: DialogInterface.OnCancelListener? = null
        ) = LoadingDialog(context, theme).apply {
            setTitle(null)
            setCancelable(cancelable)
            setOnCancelListener(cancelListener)
            addContentView(
                ProgressBar(context), ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            show()
        }
    }
}
