package com.flypika.pack.presentation.base.viewmodel

import com.flypika.pack.presentation.util.permission.OnPermissionRequestListener

interface ViewAction {

    fun showMessage(msg: String)

    fun showMessage(resId: Int)

    fun finishScreen()

    fun checkPermissions(
        permissions: Array<String>,
        onPermissionRequestListener: OnPermissionRequestListener
    )

    fun requestPermissions(
        permissions: Array<String>,
        onPermissionRequestListener: OnPermissionRequestListener
    )
}
