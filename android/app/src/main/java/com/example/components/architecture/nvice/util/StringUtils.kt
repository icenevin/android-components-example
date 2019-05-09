package com.example.components.architecture.nvice.util

import org.apache.commons.lang3.StringUtils

class StringUtils {
    companion object {
        @JvmStatic
        fun capitalize(string: String?) = StringUtils.capitalize(string?.toLowerCase())
    }
}