package com.example.components.architecture.nvice.util.extension

import com.example.components.architecture.nvice.util.StringUtils
import com.example.components.architecture.nvice.util.ValidationUtils

fun String?.isValidCitizenId() = ValidationUtils.isValidCitizenId(this)

fun String?.capitalize() = StringUtils.capitalize(this)