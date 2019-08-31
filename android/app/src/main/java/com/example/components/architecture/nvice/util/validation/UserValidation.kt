package com.example.components.architecture.nvice.util.validation

import com.example.components.architecture.nvice.data.exception.InvalidUserException
import com.example.components.architecture.nvice.data.error.UserError
import com.example.components.architecture.nvice.data.exception.ErrorException
import com.example.components.architecture.nvice.data.exception.ValidatorException
import com.example.components.architecture.nvice.model.user.User

object UserValidation {

    inline fun validateUser(user: User?, success: (User?) -> Unit, failure: (ValidatorException?) -> Unit) {
        Validator.operate(
                operation = {
                    user?.let {
                        if (it.firstName.isNullOrEmpty()) {
                            this["userFirstName"] = InvalidUserException(UserError.EMPTY_FIRST_NAME)
                        }
                        if (it.lastName.isNullOrEmpty()) {
                            this["userLastName"] = InvalidUserException(UserError.EMPTY_LAST_NAME)
                        }
                    }
                },
                success = {
                    success(user)
                },
                failure = failure
        )
    }
}