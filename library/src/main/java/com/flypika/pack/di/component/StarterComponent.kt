package com.flypika.pack.di.component

import android.content.Context
import com.flypika.pack.di.ValidatorModule
import com.flypika.pack.di.qualifier.ViewModelVariableIdQualifier
import com.flypika.pack.di.scope.StarterScope
import com.flypika.pack.presentation.util.validation.impl.EmailValidator
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ValidatorModule::class])
@StarterScope
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
