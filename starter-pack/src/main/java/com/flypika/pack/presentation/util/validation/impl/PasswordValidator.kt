package com.flypika.pack.presentation.util.validation.impl

import com.flypika.pack.presentation.util.validation.Validator

open class PasswordValidator : Validator {

    protected open val minLength = 6

    override fun validate(string: String): Boolean = string.length >= minLength
}
