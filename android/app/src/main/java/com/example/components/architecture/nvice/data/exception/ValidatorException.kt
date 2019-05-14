package com.example.components.architecture.nvice.data.exception

import com.example.components.architecture.nvice.util.validation.Validator

class ValidatorException constructor(
        val errors: HashMap<Validator, ErrorException>?
) : Exception() {

    override fun printStackTrace() {
        errors?.forEach { (_, exception) -> exception.printStackTrace() }
        super.printStackTrace()
    }
}