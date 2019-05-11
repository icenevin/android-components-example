package com.example.components.architecture.nvice.data.exception

class ValidatorException constructor(
        val list: HashMap<*, Exception>
) : Exception() {

    override fun printStackTrace() {
        list.forEach { (_, exception) -> exception.printStackTrace() }
        super.printStackTrace()
    }
}