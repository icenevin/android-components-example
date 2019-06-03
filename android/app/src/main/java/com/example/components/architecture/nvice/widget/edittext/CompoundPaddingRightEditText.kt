package com.example.components.architecture.nvice.widget.edittext

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import com.example.components.architecture.nvice.R

class CompoundPaddingRightEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = R.attr.editTextStyle,
        defStyleRes: Int = 0
) : EditText(context, attrs, defStyle, defStyleRes) {

    override fun getCompoundPaddingRight(): Int {
        return super.getCompoundPaddingRight() + if (compoundDrawables[2] == null) context.resources.getDimensionPixelSize(R.dimen.dp_36) else 0
    }
}