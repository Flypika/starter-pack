package com.flypika.pack.ui.base.fragment.shared

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.flypika.pack.ui.base.fragment.ViewModelFragment
import com.flypika.pack.ui.base.viewmodel.StarterViewModel
import com.flypika.pack.ui.base.viewmodel.ViewAction

abstract class ActivitySharedViewModelFragment<A : ViewAction, VM : StarterViewModel<A>, DB : ViewDataBinding> : ViewModelFragment<A, VM, DB>() {

    override fun createViewModel(): VM =
        ViewModelProviders.of(requireActivity(), viewModelFactory)[viewModelClass.java]
}
