package com.example.components.architecture.nvice.util.regex

object RegexUtil {

    fun generate(input: String): String {
        val filter = input.replace("[().-]".toRegex(), "")
        val inputArray = filter.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sb = StringBuilder()
        if (input.isNotEmpty()) {
            for (anInputArray in inputArray) {
                sb.append("(?=.*\\b").append(anInputArray).append(")")
            }
            sb.append(".*$")
        } else {
            sb.append(".*")
        }
        return sb.toString()
    }
}