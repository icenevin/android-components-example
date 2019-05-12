package com.example.components.architecture.nvice.data.exception

import com.example.components.architecture.nvice.data.error.Error

abstract class ErrorException
 : Exception() {
     abstract val error: Error
}