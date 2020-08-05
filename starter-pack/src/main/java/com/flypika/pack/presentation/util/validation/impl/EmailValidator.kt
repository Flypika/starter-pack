package com.flypika.pack.presentation.util.validation.impl

import android.util.Patterns
import com.flypika.pack.presentation.util.validation.Validator

class EmailValidator : Validator {

    override fun validate(string: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(string).matches()
}
