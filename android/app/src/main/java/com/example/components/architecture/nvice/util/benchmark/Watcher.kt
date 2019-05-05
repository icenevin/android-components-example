package com.example.components.architecture.nvice.util.benchmark

import timber.log.Timber

class Watcher constructor(
        val tag: String?
) {

    private var _start: Long = 0
    private var _end: Long = 0

    fun start(): Watcher {
        _start = System.currentTimeMillis()
        return this
    }

    fun stop(): Watcher {
        _end = System.currentTimeMillis()
        report("TimeCapture: $tag ----- ${_end - _start} ms")
        return this
    }

    private fun report(msg: String){
        Timber.d(msg)
    }
}