package com.flypika.pack.di.component

import android.content.Context
import com.flypika.pack.di.qualifier.ViewModelVariableIdQualifier
import com.flypika.pack.util.validation.impl.EmailValidator
import com.flypika.pack.util.validation.module.ValidatorModule
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ValidatorModule::class])
interface StarterComponent {

    @ViewModelVariableIdQualifier
    fun viewModelVariableId(): Int

    fun context(): Context

    fun emailValidator(): EmailValidator

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        @BindsInstance
        fun viewModelVariableId(@ViewModelVariableIdQualifier id: Int): Builder

        fun build(): StarterComponent
    }

    companion object {

        fun get(
            context: Context,
            viewModelVariableId: Int
        ): StarterComponent = DaggerStarterComponent.builder()
            .context(context)
            .viewModelVariableId(viewModelVariableId)
            .build()
    }
}
