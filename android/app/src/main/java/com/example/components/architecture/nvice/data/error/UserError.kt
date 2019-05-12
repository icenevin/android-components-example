package com.example.components.architecture.nvice.data.error

import androidx.annotation.StringRes
import com.example.components.architecture.nvice.R

enum class UserError(
        override val code: Int,
        @StringRes override val alertMsgRes: Int?,
        override val defMsg: String
) : Error {
    INVALID_FIRST_NAME(400, R.string.error_invalid_user_first_name, "First name is invalid."),
    INVALID_LAST_NAME(401, R.string.error_invalid_user_last_name, "Last name is invalid."),

    EMPTY_FIRST_NAME(500, R.string.error_empty_user_first_name, "First name is empty."),
    EMPTY_LAST_NAME(501, R.string.error_empty_user_last_name, "Last name is empty.");
}