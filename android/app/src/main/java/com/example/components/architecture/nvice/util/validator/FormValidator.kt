package com.example.components.architecture.nvice.util.validator

import com.example.components.architecture.nvice.data.exception.InvalidUserException
import com.example.components.architecture.nvice.data.exception.UserError
import com.example.components.architecture.nvice.data.exception.ValidatorException
import com.example.components.architecture.nvice.model.User

class FormValidator {

    companion object {
        fun validateUser(user: User?): User? {
            val exceptions = hashMapOf<UserError, Exception>()
            user?.let {
                if (it.firstName.isNullOrEmpty()) with(UserError.EMPTY_FIRST_NAME) { exceptions[this] = InvalidUserException(this) }
                if (it.lastName.isNullOrEmpty()) with(UserError.EMPTY_LAST_NAME) { exceptions[this] = InvalidUserException(this) }
                if (exceptions.isNotEmpty()) throw ValidatorException(exceptions)
            }
            return user
        }
    }
}