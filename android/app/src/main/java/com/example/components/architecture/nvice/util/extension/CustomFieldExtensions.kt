package com.example.components.architecture.nvice.util.extension

import com.example.components.architecture.nvice.data.error.Error
import com.example.components.architecture.nvice.data.exception.ErrorException
import com.example.components.architecture.nvice.data.exception.InvalidUserException
import com.example.components.architecture.nvice.util.validation.Validator
import com.example.components.architecture.nvice.widget.fields.CustomFieldEditText
import kotlin.Exception

inline fun <T : ErrorException> CustomFieldEditText.validateWith(exceptions: HashMap<Validator, T>?, action: (CustomFieldEditText, Error) -> Unit) {
    this.validateWith(exceptions, this.validator, action)
}

inline fun <T : ErrorException> CustomFieldEditText.validateWith(exceptions: HashMap<Validator, T>?, validator: Validator?, action: (CustomFieldEditText, Error) -> Unit) {
    exceptions?.let {
        (it[validator])?.let { exception ->
            action(this, exception.error)
        }
    }
}
