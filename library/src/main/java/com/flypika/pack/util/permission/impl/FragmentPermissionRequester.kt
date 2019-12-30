package com.flypika.pack.util.permission.impl

import android.content.Context
import androidx.fragment.app.Fragment
import com.flypika.pack.util.permission.BasePermissionRequester

class FragmentPermissionRequester(private val fragment: Fragment) : BasePermissionRequester() {

    override val context: Context
        get() = fragment.requireContext()

    override fun requestPermission(requestCode: Int, permissions: Array<String>) {
        fragment.requestPermissions(permissions, requestCode)
    }
}