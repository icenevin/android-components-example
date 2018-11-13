package com.example.components.architecture.nvice.executor
import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

class MainThreadExecutor : Executor {

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        mainHandler.post(command)
    }
}