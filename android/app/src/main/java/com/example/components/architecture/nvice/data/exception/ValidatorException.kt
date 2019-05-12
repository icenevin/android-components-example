package com.example.components.architecture.nvice.data.exception

import com.example.components.architecture.nvice.util.validation.Validator

class ValidatorException constructor(
        val list: HashMap<Validator, ErrorException>?
) : Exception() {

    override fun printStackTrace() {
        list?.forEach { (_, exception) -> exception.printStackTrace() }
        super.printStackTrace()
    }
}