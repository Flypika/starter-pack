package com.flypika.pack.util.validation.module

import com.flypika.pack.di.scope.StarterScope
import com.flypika.pack.util.validation.impl.EmailValidator
import dagger.Module
import dagger.Provides

@Module
internal class ValidatorModule {

    @StarterScope
    @Provides
    fun emailValidator() = EmailValidator()
}
