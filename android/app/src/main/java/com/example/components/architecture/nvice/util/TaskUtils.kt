package com.example.components.architecture.nvice.util

object TaskUtils {

    @PublishedApi
    internal var lastTimestamp = getCurrentTimestamp()

    inline fun run(delayToNext: Long, task: () -> Unit) {
        if (getCurrentTimestamp() - lastTimestamp >= delayToNext) {
            lastTimestamp = getCurrentTimestamp()
            task()
        }
    }

    @PublishedApi
    internal fun getCurrentTimestamp() = System.currentTimeMillis()
}