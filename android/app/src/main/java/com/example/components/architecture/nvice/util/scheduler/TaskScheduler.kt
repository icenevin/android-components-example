package com.example.components.architecture.nvice.util.scheduler

import android.os.Handler
import android.os.Looper
import java.io.File
import java.io.FileFilter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.regex.Pattern

interface Scheduler {

    fun execute(task: () -> Unit)

    fun postToMainThread(task: () -> Unit)

    fun postDelayedToMainThread(delay: Long, task: () -> Unit)
}

/**
 * A shim [Scheduler] that by default handles operations in the [AsyncScheduler].
 */
object DefaultScheduler : Scheduler {

    private val NUMBER_OF_THREADS: Int = getCpuCoresCount()

    private var delegate: Scheduler = AsyncScheduler

    /**
     * Sets the new delegate scheduler, null to revert to the default async one.
     */
    fun setDelegate(newDelegate: Scheduler?) {
        delegate = newDelegate ?: AsyncScheduler
    }

    override fun execute(task: () -> Unit) {
        delegate.execute(task)
    }

    override fun postToMainThread(task: () -> Unit) {
        delegate.postToMainThread(task)
    }

    override fun postDelayedToMainThread(delay: Long, task: () -> Unit) {
        delegate.postDelayedToMainThread(delay, task)
    }

    /**
     * Runs tasks in a [ExecutorService] with a fixed thread of pools
     */
    internal object AsyncScheduler : Scheduler {

        private val executorService: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        override fun execute(task: () -> Unit) {
            executorService.execute(task)
        }

        override fun postToMainThread(task: () -> Unit) {
            if (isMainThread()) {
                task()
            } else {
                val mainThreadHandler = Handler(Looper.getMainLooper())
                mainThreadHandler.post(task)
            }
        }

        private fun isMainThread(): Boolean {
            return Looper.getMainLooper().thread === Thread.currentThread()
        }

        override fun postDelayedToMainThread(delay: Long, task: () -> Unit) {
            val mainThreadHandler = Handler(Looper.getMainLooper())
            mainThreadHandler.postDelayed(task, delay)
        }
    }

    /**
     * Runs tasks synchronously.
     */
    object SyncScheduler : Scheduler {
        private val postDelayedTasks = mutableListOf<() -> Unit>()

        override fun execute(task: () -> Unit) {
            task()
        }

        override fun postToMainThread(task: () -> Unit) {
            task()
        }

        override fun postDelayedToMainThread(delay: Long, task: () -> Unit) {
            postDelayedTasks.add(task)
        }

        fun runAllScheduledPostDelayedTasks() {
            val tasks = postDelayedTasks.toList()
            clearScheduledPostdelayedTasks()
            for (task in tasks) {
                task()
            }
        }

        fun clearScheduledPostdelayedTasks() {
            postDelayedTasks.clear()
        }
    }

    /**
     * Get [NUMBER_OF_THREADS] from device's info
     */
    private fun getCpuCoresCount(): Int {
        class CpuFilter : FileFilter {
            override fun accept(pathname: File): Boolean {
                return Pattern.matches("cpu[0-9]+", pathname.name) // check if filename is "cpu", followed by one or more digits
            }
        }

        return try {
            val dir = File("/sys/devices/system/cpu/") // get directory containing CPU info
            val files = dir.listFiles(CpuFilter())
            files.size
        } catch (e: Exception) {
            1 // default: 1 core
        }

    }
}