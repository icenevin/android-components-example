package com.example.components.architecture.nvice.data.exception

class ValidatorException constructor(
        val errors: HashMap<String, ErrorException>?
) : Exception() {

    override fun printStackTrace() {
        errors?.forEach { (_, exception) -> exception.printStackTrace() }
        super.printStackTrace()
    }
}