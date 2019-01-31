package com.example.components.architecture.nvice.widget.fields

import android.content.Context
import android.support.annotation.ColorRes
import android.util.AttributeSet
import android.widget.LinearLayout

abstract class CustomField @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    abstract fun setIconTint(@ColorRes color: Int)
}