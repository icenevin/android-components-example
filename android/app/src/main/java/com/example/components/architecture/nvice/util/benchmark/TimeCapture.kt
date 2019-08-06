package com.example.components.architecture.nvice.util.benchmark

import android.os.Handler

class TimeCapture {

    companion object {

        @PublishedApi
        internal const val WATCHER_TIME_OUT = 10000L // limit time out at 10 seconds

        @PublishedApi
        internal val watchers = LinkedHashMap<String, Watcher?>()

        fun capture(task: () -> Unit) {
            this.capture(task.toString(), task)
        }

        fun capture(tag: String, task: () -> Unit) {
            start(tag)
            task()
            stop(tag)
        }

        fun captureAsync(task: Companion.(Watcher?) -> Unit) {
            this.captureAsync(task.toString(), task)
        }

        inline fun captureAsync(tag: String, task: Companion.(Watcher?) -> Unit) {
            start(tag)
            task(this, watchers[tag])
            Handler().postDelayed({
                cancel(tag)
            }, WATCHER_TIME_OUT)
        }

        fun start(tag: String) {
            watchers[tag] = Watcher(tag).start()
        }

        fun stop() {
            val lastKey = watchers.keys.last()
            stop(lastKey)
        }

        fun stop(tag: String?) {
            tag?.let {
                watchers[it]?.stop()
                watchers[it] = null
                watchers.remove(it)
            }
        }

        fun stop(watcher: Watcher?) {
            val tag = watcher?.tag
            this.stop(tag)
        }

        fun cancel() {
            val lastKey = watchers.keys.last()
            cancel(lastKey)
        }

        fun cancel(tag: String?) {
            tag?.let {
                watchers[it] = null
                watchers.remove(it)
            }
        }

        fun clear() {
            watchers.clear()
        }
    }
}