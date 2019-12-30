package com.flypika.pack.ui.base.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.flypika.pack.ui.dialog.LoadingDialog
import com.flypika.pack.ui.util.transaction
import com.flypika.pack.util.permission.impl.FragmentPermissionRequester

abstract class BaseFragment : Fragment() {

    protected val permissionRequester = FragmentPermissionRequester(this)

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
}