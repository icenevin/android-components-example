package com.example.components.architecture.nvice.util.extension

import android.widget.EditText
import com.example.components.architecture.nvice.data.error.Error
import com.example.components.architecture.nvice.data.exception.ErrorException
import com.example.components.architecture.nvice.util.validation.Validator

fun <T : ErrorException> EditText.validateWith(exceptions: HashMap<Validator, T>?, validator: Validator?, action: (EditText, Error) -> Unit) {
    exceptions?.let {
        (it[validator])?.let { exception ->
            action(this, exception.error)
        }
    }
}