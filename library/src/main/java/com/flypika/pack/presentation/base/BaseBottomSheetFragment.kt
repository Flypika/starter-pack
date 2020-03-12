package com.flypika.pack.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.reflect.KClass

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    private val _viewModels by lazy { provideViewModel().mapKeys { it.key.java.name } }

    abstract fun viewModel(): BaseViewModel

    fun provideViewModel(): Map<KClass<*>, BaseViewModel> {
        return mapOf(
            vmCreator(viewModel()::class, viewModel())
        )
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ignoring = skipAutoObserveForVmActions()
        _viewModels.forEach {
            if (ignoring.firstOrNull { cl -> cl.java.name == it.key } != null) {
                return@forEach
            }

            observeVmActions(it.value)
        }
    }

    fun <T : BaseViewModel> vmCreator(
        cl: KClass<T>,
        viewModel: BaseViewModel
    ): Pair<KClass<*>, BaseViewModel> {
        return cl to viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    open fun skipAutoObserveForVmActions(): List<KClass<*>> = emptyList()

    fun observeVmActions(vm: BaseViewModel) {
        vm.activityActionBehavior.observe(this, Observer {
            it?.invoke(activity as? AppCompatActivity ?: return@Observer)
        })
    }
}