package com.example.components.architecture.nvice.util

class TaskUtil {
    companion object {
        private var lastTimestamp = getCurrentTimestamp()

        fun run(delayToNext: Long, task: () -> Unit) {
            if (getCurrentTimestamp() - lastTimestamp >= delayToNext) {
                lastTimestamp = getCurrentTimestamp()
                task()
            }
        }

        private fun getCurrentTimestamp() = System.currentTimeMillis()
    }
}