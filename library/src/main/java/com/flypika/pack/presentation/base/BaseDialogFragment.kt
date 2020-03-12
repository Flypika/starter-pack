package com.flypika.pack.presentation.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import kotlin.reflect.KClass

abstract class BaseDialogFragment : DialogFragment() {

    private val _viewModels by lazy { provideViewModel().mapKeys { it.key.java.name } }

    abstract fun viewModel(): BaseViewModel

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    fun provideViewModel(): Map<KClass<*>, BaseViewModel> {
        return mapOf(
            vmCreator(viewModel()::class, viewModel())
        )
    }

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
        vm.activityActionBehavior.observe(this@BaseDialogFragment, Observer {
            it?.invoke(activity as? AppCompatActivity ?: return@Observer)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        _viewModels.values.forEach {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        _viewModels.values.forEach {
            it.onPermissionActivityResult(requestCode, permissions, grantResults)
        }
    }
}
