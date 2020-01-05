package com.flypika.pack.ui.base.activity

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.flypika.pack.ui.base.fragment.OnBackListener
import com.flypika.pack.ui.dialog.LoadingDialog
import com.flypika.pack.ui.util.transaction
import com.flypika.pack.util.permission.impl.ActivityPermissionRequester
import dagger.android.support.DaggerAppCompatActivity

abstract class StarterActivity : DaggerAppCompatActivity() {

    protected val permissionRequester = ActivityPermissionRequester(this)

    private var loadingDialog: LoadingDialog? = null

    protected val loadingObserver = Observer<Boolean> { value: Boolean? ->
        if (value != null && value) {
            showLoadingDialog()
        } else {
            hideLoadingDialog()
        }
    }

    override fun onBackPressed() {
        var shouldCallSuper = true
        supportFragmentManager.fragments
            .filter { it.isVisible }
            .forEach {
                if (it is OnBackListener && it.onBackPressed()) {
                    shouldCallSuper = false
                }
            }

        if (shouldCallSuper) {
            super.onBackPressed()
        }
    }

    protected open fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.show(this)
        }
    }

    protected open fun hideLoadingDialog() {
        loadingDialog?.hide()
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    /**
     * Shows fragment in specific contentFrame
     *
     * @param contentFrame   - container res id to show fragment in
     * @param fragment       - fragment to show
     * @param addToBackStack - whether this fragment should be added to back stack or not
     */
    protected open fun showFragment(
        fragment: Fragment,
        @IdRes contentFrame: Int,
        addToBackStack: Boolean = false,
        block: (FragmentTransaction.() -> Unit)? = null
    ) {
        supportFragmentManager.transaction {
            if (addToBackStack) {
                addToBackStack(fragment.javaClass.canonicalName)
            }
            replace(contentFrame, fragment)
            block?.invoke(this)
        }
    }

    protected open fun popBackStack(
        popFragmentClass: Class<*>,
        inclusive: Boolean
    ) {
        supportFragmentManager.popBackStack(
            popFragmentClass.canonicalName,
            if (inclusive) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0
        )
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