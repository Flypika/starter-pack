package com.flypika.pack.ui.base.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.flypika.pack.R
import com.flypika.pack.ui.dialog.LoadingDialog
import com.flypika.pack.ui.util.transaction
import com.flypika.pack.util.permission.OnPermissionRequestListener
import com.flypika.pack.util.permission.impl.FragmentPermissionRequester
import dagger.android.support.DaggerFragment

abstract class StarterFragment : DaggerFragment() {

    private val permissionRequester = FragmentPermissionRequester(this)

    private var loadingDialog: LoadingDialog? = null

    protected val loadingObserver = Observer<Boolean> { value: Boolean? ->
        if (value != null && value) {
            showLoadingDialog()
        } else {
            hideLoadingDialog()
        }
    }

    protected fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.show(requireContext())
        }
    }

    protected fun hideLoadingDialog() {
        loadingDialog?.hide()
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    protected open fun showFragment(
        contentFrame: Int,
        fragment: Fragment,
        addToBackStack: Boolean = false
    ) {
        childFragmentManager.transaction {
            if (addToBackStack) {
                addToBackStack(fragment.javaClass.canonicalName)
            }
            replace(contentFrame, fragment)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (!permissionRequester.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    open fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    open fun showMessage(resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
    }

    open fun showUnknownError() {
        showMessage(R.string.unknown_error)
    }

    open fun finishScreen() {
        parentFragment?.childFragmentManager?.popBackStack() ?: kotlin.run {
            if (activity?.supportFragmentManager?.backStackEntryCount ?: 0 > 0) {
                activity?.supportFragmentManager?.popBackStack()
            } else {
                activity?.onBackPressed()
            }
        }
    }

    open fun checkPermission(
        permissions: Array<String>,
        onPermissionRequestListener: OnPermissionRequestListener
    ) {
        permissionRequester.tryRequestPermission(permissions, onPermissionRequestListener)
    }
}