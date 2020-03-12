package com.flypika.pack.presentation.util.validation

abstract class DecoratableValidator(private val validator: Validator? = null) : Validator {

    protected abstract fun decorateValidate(string: String): Boolean

    final override fun validate(string: String): Boolean {
        return validator?.validate(string) != false && decorateValidate(string)
    }
}
