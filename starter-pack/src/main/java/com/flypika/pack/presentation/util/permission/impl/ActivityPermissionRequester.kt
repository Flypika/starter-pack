package com.flypika.pack.presentation.util.permission.impl

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import com.flypika.pack.presentation.util.permission.BasePermissionRequester

class ActivityPermissionRequester(private val activity: Activity) : BasePermissionRequester() {

    override val context: Context
        get() = activity

    override fun requestPermission(requestCode: Int, permissions: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }
}