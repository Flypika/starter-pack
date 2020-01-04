package com.flypika.pack.di.component

import android.content.Context
import com.flypika.pack.di.qualifier.ViewModelVariableIdQualifier
import dagger.BindsInstance
import dagger.Component

@Component
interface BaseComponent {

    @ViewModelVariableIdQualifier
    fun viewModelVariableId(): Int

    fun context(): Context

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        @BindsInstance
        fun viewModelVariableId(@ViewModelVariableIdQualifier id: Int): Builder

        fun build(): BaseComponent
    }

    companion object {

        fun get(
            context: Context,
            viewModelVariableId: Int
        ): BaseComponent = DaggerBaseComponent.builder()
            .context(context)
            .viewModelVariableId(viewModelVariableId)
            .build()
    }
}
