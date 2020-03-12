package com.flypika.pack.presentation.base

import androidx.appcompat.app.AppCompatActivity
import com.flypika.pack.presentation.ext.cast

open class VmAction(var singleAction: (BaseActivity) -> Unit) {

    open fun invoke(activity: AppCompatActivity?) {
        activity ?: return
        activity.cast(BaseActivity::class)?.let {
            singleAction(it)
        }
        singleAction = {}
    }
}

class VmActionCompat(
    var action: (AppCompatActivity) -> Unit
) : VmAction({}) {

    override fun invoke(activity: AppCompatActivity?) {
        activity ?: return
        action(activity)
        action = {}
    }
}