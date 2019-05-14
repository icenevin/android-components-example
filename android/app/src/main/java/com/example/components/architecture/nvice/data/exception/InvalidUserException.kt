package com.example.components.architecture.nvice.data.exception

import com.example.components.architecture.nvice.data.error.UserError

class InvalidUserException(
        override val error: UserError
) : ErrorException()