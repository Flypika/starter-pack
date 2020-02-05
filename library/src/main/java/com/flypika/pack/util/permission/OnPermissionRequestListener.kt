package com.flypika.pack.util.permission

interface OnPermissionRequestListener {
    fun onPermissionsGranted(permissions: List<String>) = Unit
    fun onPermissionsDenied(permissions: List<String>) = Unit
}
