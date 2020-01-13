package com.flypika.pack.util.validation.impl

import com.flypika.pack.util.validation.Validator

open class PasswordValidator : Validator {

    protected open val minLength = 6

    override fun validate(string: String): Boolean = string.length >= minLength
}
