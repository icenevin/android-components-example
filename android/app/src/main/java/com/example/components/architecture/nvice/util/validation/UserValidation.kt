package com.example.components.architecture.nvice.util.validation

import com.example.components.architecture.nvice.data.exception.InvalidUserException
import com.example.components.architecture.nvice.data.error.UserError
import com.example.components.architecture.nvice.data.exception.ErrorException
import com.example.components.architecture.nvice.data.exception.ValidatorException
import com.example.components.architecture.nvice.model.User

class UserValidation {

    companion object {

        inline fun validateUser(user: User?, success: (User?) -> Unit, fail: (ValidatorException?) -> Unit) {
            try {
                validateUser(user).run(success)
            } catch (exception: ValidatorException) {
                exception.printStackTrace()
                fail(exception)
            }
        }

        @PublishedApi
        internal fun validateUser(user: User?): User? {
            val exceptions = hashMapOf<Validator, ErrorException>()
            user?.let {
                if (it.firstName.isNullOrEmpty()) {
                    exceptions[Validator.USER_FIRST_NAME] = InvalidUserException(UserError.EMPTY_FIRST_NAME)
                }
                if (it.lastName.isNullOrEmpty()) {
                    exceptions[Validator.USER_LAST_NAME] = InvalidUserException(UserError.EMPTY_LAST_NAME)
                }
                if (exceptions.isNotEmpty()) {
                    throw ValidatorException(exceptions)
                }
            }
            return user
        }
    }
}