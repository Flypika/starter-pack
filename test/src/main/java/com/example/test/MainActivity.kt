package com.example.test

import androidx.lifecycle.ViewModelProviders
import com.example.test.databinding.ActivityMainBinding
import com.flypika.pack.ui.base.activity.BaseViewModelActivity

class MainActivity : BaseViewModelActivity<MainViewModel.MainViewAction, MainViewModel, ActivityMainBinding>(), MainViewModel.MainViewAction {

    override val viewActionHandler: MainViewModel.MainViewAction
        get() = this
    override val viewModelVariableId: Int
        get() = BR.viewModel

    override fun createViewModel(): MainViewModel {
        return ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    override val layoutResId: Int
        get() = R.layout.activity_main
}
