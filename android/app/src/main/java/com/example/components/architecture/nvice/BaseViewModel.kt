package com.example.components.architecture.nvice

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    open fun disposeServices() {}

    override fun onCleared() {
        disposeServices()
        super.onCleared()
    }
}