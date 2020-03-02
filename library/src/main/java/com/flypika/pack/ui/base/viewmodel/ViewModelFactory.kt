package com.flypika.pack.ui.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory<VM : StarterViewModel<*>> @Inject constructor(private val viewModel: Provider<VM>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        modelClass.cast(
            viewModel.get().also { it.onInjected() }
        ) ?: throw ClassCastException()
}
