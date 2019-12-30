package com.flypika.pack.ui.base.activity

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.flypika.pack.R
import com.flypika.pack.ui.base.viewmodel.BaseViewAction
import com.flypika.pack.ui.base.viewmodel.BaseViewModel
import com.flypika.pack.util.permission.OnPermissionRequestListener

abstract class BaseViewModelActivity<
        A : BaseViewAction,
        VM : BaseViewModel<A>,
        DB : ViewDataBinding
        > : BaseDataBindingActivity<DB>(), BaseViewAction {

    protected lateinit var viewModel: VM
        private set

    protected abstract val viewActionHandler: A

    protected abstract val viewModelVariableId: Int

    protected abstract fun createViewModel(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        binding.setVariable(viewModelVariableId, viewModel)
        setupViewActionObserver()
    }

    private fun setupViewActionObserver() {
        viewModel.viewActionManager.addObserver(lifecycle) { action ->
            action(viewActionHandler)
        }
    }

    override fun showAPIError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun showMessage(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
    }

    override fun showUnknownError() {
        showMessage(R.string.unknown_error)
    }

    override fun finishScreen() {
        finish()
    }

    override fun checkPermission(
        permissions: Array<String>,
        onPermissionRequestListener: OnPermissionRequestListener
    ) {
        permissionRequester.tryRequestPermission(permissions, onPermissionRequestListener)
    }
}
