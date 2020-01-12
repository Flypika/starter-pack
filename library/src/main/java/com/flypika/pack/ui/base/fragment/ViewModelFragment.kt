package com.flypika.pack.ui.base.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.flypika.pack.di.qualifier.ViewModelVariableIdQualifier
import com.flypika.pack.ui.base.viewmodel.StarterViewModel
import com.flypika.pack.ui.base.viewmodel.ViewAction
import com.flypika.pack.ui.base.viewmodel.ViewModelFactory
import com.flypika.pack.util.permission.OnPermissionRequestListener
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class ViewModelFragment<A : ViewAction, VM : StarterViewModel<A>, DB : ViewDataBinding>
    : DataBindingFragment<DB>(), ViewAction {

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
        observeLoading()
    }

    private fun observeLoading() {
        viewModel.loadingLiveData.observe(this, loadingObserver)
    }

    private fun setupViewActionObserver() {
        viewModel.viewActionManager.addObserver(viewLifecycleOwner.lifecycle) { action ->
            action(viewActionHandler)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(viewModelVariableId, viewModel)
    }

    override fun showMessage(msg: String) = super.showMessage(msg)

    override fun showMessage(resId: Int) = super.showMessage(resId)

    override fun showUnknownError() = super.showUnknownError()

    override fun finishScreen() = super.finishScreen()

    override fun checkPermission(
        permissions: Array<String>,
        onPermissionRequestListener: OnPermissionRequestListener
    ) = super.checkPermission(permissions, onPermissionRequestListener)
}