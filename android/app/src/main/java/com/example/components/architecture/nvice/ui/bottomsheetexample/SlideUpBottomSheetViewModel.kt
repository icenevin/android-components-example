package com.example.components.architecture.nvice.ui.bottomsheetexample

import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class SlideUpBottomSheetViewModel @Inject constructor() : ViewModel() {

    fun getExampleText() = "Congratulation!!"
}