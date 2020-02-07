package com.flypika.pack.util.permission

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.util.SparseArray
import androidx.core.content.ContextCompat
import com.flypika.pack.ext.TAG
import java.util.*

abstract class BasePermissionRequester {

    private var currentRequestCode: Short = 1
    private val permissionsMap = SparseArray<HashSet<String>>()
    private val listenersMap = SparseArray<OnPermissionRequestListener>()

    protected abstract val context: Context

    protected abstract fun requestPermission(
        requestCode: Int,
        permissions: Array<String>
    )

    private fun bindPermissionsWithRequestCode(
        permissions: HashSet<String>,
        listener: OnPermissionRequestListener
    ): Int {
        checkRequestCode()
        val requestCode = currentRequestCode++.toInt()
        permissionsMap.append(requestCode, permissions)
        listenersMap.append(requestCode, listener)
        return requestCode
    }

    private fun checkRequestCode() {
        if (currentRequestCode < 1) {
            currentRequestCode = 1
        }
    }

    fun tryRequestPermission(
        arrayPermissions: Array<String>,
        listener: OnPermissionRequestListener
    ) {
        if (!isNeedHandlePermissions) {
            listener.onPermissionsGranted(listOf(*arrayPermissions))
        }
        val granted: MutableList<String> = ArrayList()
        val denied = HashSet<String>()
        for (permission in arrayPermissions) {
            if (isPermissionGranted(permission)) {
                granted.add(permission)
            } else {
                denied.add(permission)
            }
        }
        if (granted.isNotEmpty()) {
            listener.onPermissionsGranted(granted)
        }
        if (denied.isNotEmpty()) {
            val requestCode = bindPermissionsWithRequestCode(denied, listener)
            requestPermission(requestCode, denied.toTypedArray())
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        val requestedPermissions = permissionsMap[requestCode]
        if (requestedPermissions == null) {
            Log.e(TAG, "could not find requestedPermissions for request code=$requestCode")
            return false
        } else {
            permissionsMap.delete(requestCode)
        }
        val granted: MutableList<String> = ArrayList()
        val denied: MutableList<String> = ArrayList()
        for (i in permissions.indices) {
            val permission = permissions[i]
            if (!requestedPermissions.contains(permission)) {
                Log.e(
                    TAG,
                    "permissions contain permission that not included in requestedPermissions. permission=$permission"
                )
                continue
            }
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(permission)
            } else {
                denied.add(permission)
            }
        }
        val listener = listenersMap[requestCode]
        if (listener == null) {
            Log.e(TAG, "could not find listener for request code=$requestCode")
            return false
        } else {
            listenersMap.delete(requestCode)
        }
        if (granted.isNotEmpty()) {
            listener.onPermissionsGranted(granted)
        }
        if (denied.isNotEmpty()) {
            listener.onPermissionsDenied(denied)
        }
        return true
    }

    private fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private val isNeedHandlePermissions: Boolean =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
}