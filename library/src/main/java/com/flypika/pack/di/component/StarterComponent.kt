package com.flypika.pack.di.component

import android.content.Context
import com.flypika.pack.di.qualifier.ViewModelVariableIdQualifier
import dagger.BindsInstance
import dagger.Component

@Component
interface StarterComponent {

    @ViewModelVariableIdQualifier
    fun viewModelVariableId(): Int

    fun context(): Context

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
