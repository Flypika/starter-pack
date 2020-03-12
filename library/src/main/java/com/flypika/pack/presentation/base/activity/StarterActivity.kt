package com.flypika.pack.presentation.base.activity

import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.flypika.pack.presentation.base.fragment.OnBackListener
import com.flypika.pack.presentation.dialog.LoadingDialog
import com.flypika.pack.presentation.ui.util.transaction
import com.flypika.pack.presentation.util.permission.OnPermissionRequestListener
import com.flypika.pack.presentation.util.permission.impl.ActivityPermissionRequester
import dagger.android.support.DaggerAppCompatActivity


abstract class StarterActivity : DaggerAppCompatActivity() {

    private val permissionRequester = ActivityPermissionRequester(this)

    fun onBackPressedDefault() = super.onBackPressed()

    protected var loadingDialog: LoadingDialog? = null

    protected open val loadingObserver = Observer<Boolean> { value: Boolean? ->
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
            for (child in supportFragmentManager.fragments) {
                if (child.isVisible) {
                    child.childFragmentManager.let {
                        if (it.backStackEntryCount > 0) {
                            it.popBackStack()
                            return
                        }
                    }
                }
            }
            onBackPressedDefault()
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

    open fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    open fun showMessage(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
    }

    open fun checkPermissions(
        permissions: Array<String>,
        onPermissionRequestListener: OnPermissionRequestListener
    ) {
        permissionRequester.checkPermissions(permissions, onPermissionRequestListener)
    }

    open fun requestPermissions(
        permissions: Array<String>,
        onPermissionRequestListener: OnPermissionRequestListener
    ) {
        permissionRequester.tryRequestPermission(permissions, onPermissionRequestListener)
    }
}