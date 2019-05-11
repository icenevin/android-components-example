package com.example.components.architecture.nvice.data.exception

class InvalidUserException(
        val error: UserError
) : Exception() {

    override fun printStackTrace() {
        System.err.println("$this (${error.code}): ${error.msg}\n\tat ${this.stackTrace[0]}\n\tat ${this.stackTrace[1]}")
    }
}

enum class UserError(
        val code: Int,
        val msg: String
) {
    INVALID_FIRST_NAME(400,"First name is invalid."),
    INVALID_LAST_NAME(401,"Last name is invalid."),

    EMPTY_FIRST_NAME(500,"First name is empty."),
    EMPTY_LAST_NAME(501,"Last name is empty."),
}