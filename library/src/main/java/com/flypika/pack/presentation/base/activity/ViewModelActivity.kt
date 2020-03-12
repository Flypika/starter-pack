package com.flypika.pack.presentation.base.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.flypika.pack.di.qualifier.ViewModelVariableIdQualifier
import com.flypika.pack.presentation.base.viewmodel.StarterViewModel
import com.flypika.pack.presentation.base.viewmodel.ViewAction
import com.flypika.pack.presentation.base.viewmodel.ViewModelFactory
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class ViewModelActivity<A : ViewAction, VM : StarterViewModel<A>, DB : ViewDataBinding>
    : DataBindingActivity<DB>(), ViewAction {

    protected val viewModel: VM by lazy { createViewModel() }

    protected abstract val viewActionHandler: A

    @JvmField
    @field:[Inject ViewModelVariableIdQualifier]
    protected var viewModelVariableId: Int = 0

    protected abstract val viewModelClass: KClass<VM>

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(viewModelVariableId, viewModel)
        setupViewActionObserver()
        observeLoading()
    }

    private fun observeLoading() {
        viewModel.loadingLiveData.observe(this, loadingObserver)
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

}