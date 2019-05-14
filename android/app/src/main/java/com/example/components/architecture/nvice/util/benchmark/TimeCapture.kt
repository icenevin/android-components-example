package com.example.components.architecture.nvice.util.benchmark

import android.os.Handler

class TimeCapture {

    companion object {

        @PublishedApi
        internal const val WATCHER_TIME_OUT = 10000L

        @PublishedApi
        internal val watchers = HashMap<String, Watcher?>()

        fun start(tag: String) {
            watchers[tag] = Watcher(tag).start()
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


        fun cancel(tag: String?) {
            tag?.let {
                watchers[it] = null
                watchers.remove(it)
            }
        }

        fun clear() {
            watchers.clear()
        }

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
    }
}