package com.flypika.pack.ui.base.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.flypika.pack.di.qualifier.ViewModelVariableIdQualifier
import com.flypika.pack.ui.base.viewmodel.StarterViewModel
import com.flypika.pack.ui.base.viewmodel.ViewAction
import com.flypika.pack.ui.base.viewmodel.ViewModelFactory
import com.flypika.pack.util.permission.OnPermissionRequestListener
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class ViewModelActivity<A : ViewAction, VM : StarterViewModel<A>, DB : ViewDataBinding>
    : DataBindingActivity<DB>(), ViewAction {

    protected lateinit var viewModel: VM
        private set

    protected abstract val viewActionHandler: A

    @JvmField
    @field:[Inject ViewModelVariableIdQualifier]
    protected var viewModelVariableId: Int = 0

    protected abstract val viewModelClass: KClass<VM>

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        binding.setVariable(viewModelVariableId, viewModel)
        setupViewActionObserver()
    }

    protected open fun createViewModel(): VM =
        ViewModelProviders.of(this, viewModelFactory)[viewModelClass.java]

    private fun setupViewActionObserver() {
        viewModel.viewActionManager.addObserver(lifecycle) { action ->
            action(viewActionHandler)
        }
    }

    override fun finishScreen() {
        finish()
    }

    override fun showMessage(msg: String) = super.showMessage(msg)

    override fun showMessage(resId: Int) = super.showMessage(resId)

    override fun showUnknownError() = super.showUnknownError()

    override fun checkPermission(
        permissions: Array<String>,
        onPermissionRequestListener: OnPermissionRequestListener
    ) = super.checkPermission(permissions, onPermissionRequestListener)
}
