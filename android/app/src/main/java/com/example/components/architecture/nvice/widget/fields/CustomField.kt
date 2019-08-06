package com.example.components.architecture.nvice.widget.fields

import android.content.Context
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet

abstract class CustomField @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    var validator: String? = null

    abstract fun setIconTint(@ColorRes color: Int)
    abstract fun setDrawableEndTint(@ColorRes color: Int)
    abstract fun setOnClick(listener: OnClickListener)
    abstract fun setOnDrawableEndClick(listener: OnClickListener)
}