package com.example.components.architecture.nvice.util

import com.example.components.architecture.nvice.data.exception.InvalidUserException
import com.example.components.architecture.nvice.data.exception.UserError
import com.example.components.architecture.nvice.data.exception.ValidatorException
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.util.extension.getInt
import com.example.components.architecture.nvice.util.regex.RegexPattern
import timber.log.Timber

class ValidationUtils {

    companion object {
        fun isValidCitizenId(id: String?): Boolean {
            id?.let {
                if (id.length == 13) {
                    try {
                        var sum = 0
                        for (index in 1..12) {
                            sum += (14 - index) * id[index - 1].getInt()
                        }
                        (sum % 11).let {
                            if ((if (it > 1) 11 else 1) - it == id.last().getInt()) {
                                Timber.i("Citizen id is detected\n\tresult: $id")
                                return true
                            }
                        }
                    } catch (nfe: NumberFormatException) {
                        Timber.i("Input is not a number")
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
            return false
        }

        fun isValidEmail(email: String?): Boolean {
            email?.let {
                return it.matches(RegexPattern.EMAIL.regex())
            }
            return false
        }

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