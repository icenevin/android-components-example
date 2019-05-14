package com.example.components.architecture.nvice.data.exception

import com.example.components.architecture.nvice.data.error.Error

abstract class ErrorException : Exception() {

    abstract val error: Error

    override fun printStackTrace() {
        System.err.println("$this (${error.code}): ${error.defMsg}\n\tat ${this.stackTrace[0]}\n\tat ${this.stackTrace[1]}")
    }
}