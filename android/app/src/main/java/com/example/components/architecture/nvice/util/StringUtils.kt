package com.example.components.architecture.nvice.util

import org.apache.commons.lang3.StringUtils
import java.util.*

object StringUtils {

    @JvmStatic
    fun capitalize(string: String?) = StringUtils.capitalize(string?.toLowerCase(Locale.getDefault()))
}