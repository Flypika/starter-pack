package com.flypika.pack.ui.base.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.flypika.pack.R
import com.flypika.pack.di.qualifier.ViewModelVariableIdQualifier
import com.flypika.pack.ui.base.viewmodel.ViewAction
import com.flypika.pack.ui.base.viewmodel.StarterViewModel
import com.flypika.pack.ui.base.viewmodel.ViewModelFactory
import com.flypika.pack.util.permission.OnPermissionRequestListener
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class ViewModelFragment<
        A : ViewAction,
        VM : StarterViewModel<A>,
        DB : ViewDataBinding
        > : DataBindingFragment<DB>(), ViewAction {

    protected lateinit var viewModel: VM
        private set

    protected abstract var viewActionHandler: A

    @JvmField
    @field:[Inject ViewModelVariableIdQualifier]
    protected var viewModelVariableId: Int = 0

    protected abstract val viewModelClass: KClass<VM>

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory<VM>

    protected open fun createViewModel(): VM =
        ViewModelProviders.of(this, viewModelFactory)[viewModelClass.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        setupViewActionObserver()
    }

    private fun setupViewActionObserver() {
        viewModel.viewActionManager.addObserver(viewLifecycleOwner.lifecycle) { action ->
            action(viewActionHandler)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(viewModelVariableId, viewModel)
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun showMessage(resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
    }

    override fun showUnknownError() {
        showMessage(R.string.unknown_error)
    }

    override fun finishScreen() {
        parentFragment?.childFragmentManager?.popBackStack() ?: kotlin.run {
            if (activity?.supportFragmentManager?.backStackEntryCount ?: 0 > 0) {
                activity?.supportFragmentManager?.popBackStack()
            } else {
                activity?.onBackPressed()
            }
        }
    }

    override fun checkPermission(
        permissions: Array<String>,
        onPermissionRequestListener: OnPermissionRequestListener
    ) {
        permissionRequester.tryRequestPermission(permissions, onPermissionRequestListener)    }
}