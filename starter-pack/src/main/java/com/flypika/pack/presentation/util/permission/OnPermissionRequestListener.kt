package com.flypika.pack.presentation.util.permission

interface OnPermissionRequestListener {
    fun onPermissionsGranted(permissions: List<String>) = Unit
    fun onPermissionsDenied(permissions: List<String>) = Unit
}
