package com.example.components.architecture.nvice.util.extension

import com.example.components.architecture.nvice.data.error.Error
import com.example.components.architecture.nvice.data.exception.ErrorException
import com.example.components.architecture.nvice.widget.fields.CustomFieldEditText

inline fun <T : ErrorException> CustomFieldEditText.validateWith(exceptions: HashMap<String, T>?, action: (CustomFieldEditText, Error) -> Unit) {
    this.validateWith(exceptions, this.validator, action)
}

inline fun <T : ErrorException> CustomFieldEditText.validateWith(exceptions: HashMap<String, T>?, validatorName: String?, action: (CustomFieldEditText, Error) -> Unit) {
    exceptions?.let {
        (it[validatorName])?.let { exception ->
            action(this, exception.error)
        }
    }
}
