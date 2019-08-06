package com.example.components.architecture.nvice.util.extension

import android.widget.EditText
import com.example.components.architecture.nvice.data.error.Error
import com.example.components.architecture.nvice.data.exception.ErrorException

inline fun <T : ErrorException> EditText.validateWith(exceptions: HashMap<String, T>?, validator: String?, action: (EditText, Error) -> Unit) {
    exceptions?.let {
        (it[validator])?.let { exception ->
            action(this, exception.error)
        }
    }
}