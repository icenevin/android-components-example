package com.example.components.architecture.nvice.data.exception

import com.example.components.architecture.nvice.data.error.UserError

class InvalidUserException(
        override val error: UserError
) : ErrorException() {

    override fun printStackTrace() {
        System.err.println("$this (${error.code}): ${error.defMsg}\n\tat ${this.stackTrace[0]}\n\tat ${this.stackTrace[1]}")
    }
}