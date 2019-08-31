package com.example.components.architecture.nvice.util.validation

import com.example.components.architecture.nvice.data.exception.ErrorException
import com.example.components.architecture.nvice.data.exception.ValidatorException

object Validator {

    inline fun operate(operation: HashMap<String, ErrorException>.() -> Unit, success: () -> Unit, failure: (ValidatorException?) -> Unit) {
        try {
            perform {
                apply {
                    operation(this)
                }
            }.finally {
                success()
            }
        } catch (exception: ValidatorException) {
            exception.printStackTrace()
            failure(exception)
        }
    }

    inline fun perform(action: HashMap<String, ErrorException>.() -> HashMap<String, ErrorException>): Boolean {
        val exceptions = action(hashMapOf())
        if (exceptions.isNotEmpty()) {
            throw ValidatorException(exceptions)
        }
        return exceptions.isNullOrEmpty()
    }

    inline fun Boolean.finally(action: () -> Unit) {
        if (this) action()
    }
}