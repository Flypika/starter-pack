package com.flypika.pack.ui.base.viewmodel

import com.flypika.pack.util.permission.OnPermissionRequestListener

interface ViewAction {

    fun showMessage(msg: String)

    fun showMessage(resId: Int)

    fun finishScreen()

    fun checkPermission(
        permissions: Array<String>,
        onPermissionRequestListener: OnPermissionRequestListener
    )
}
