package com.example.components.architecture.nvice.util.extension

import com.example.components.architecture.nvice.util.StringUtil
import com.example.components.architecture.nvice.util.ValidationUtil

fun String?.isValidCitizenId() = ValidationUtil.isValidCitizenId(this)

fun String?.capitalize() = StringUtil.capitalize(this)