package com.example.components.architecture.nvice.widget.fields

import android.content.Context
import android.support.annotation.ColorRes
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.LinearLayout

abstract class CustomField @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    abstract fun setIconTint(@ColorRes color: Int)
    abstract fun setDrawableEndTint(@ColorRes color: Int)
    abstract fun setOnClick(listener: OnClickListener)
    abstract fun setOnDrawableEndClick(listener: OnClickListener)
}