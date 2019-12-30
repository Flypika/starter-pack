package com.flypika.pack.util.permission

interface OnPermissionRequestListener {
    fun onPermissionsGranted(permissions: List<String>)
    fun onPermissionsDenied(permissions: List<String>)
}
