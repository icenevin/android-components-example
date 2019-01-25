package com.example.components.architecture.nvice.ui.user.create

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.components.architecture.nvice.R
import kotlinx.android.synthetic.main.view_user_create_input_spinner.view.*

class UserCreateInputSpinner @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_user_create_input_spinner, this, true)
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.UserCreateSpinner, 0, 0)
            val fullWith = attributes.getBoolean(R.styleable.UserCreateSpinner_spinner_fullWidth, false)
            spSelectField.layoutParams = LinearLayout.LayoutParams(spSelectField.width, LayoutParams.MATCH_PARENT, if (fullWith) 2f else 1f)
            val iconPadding = attributes.getLayoutDimension(R.styleable.UserCreateSpinner_spinner_iconPadding, 0)
            ivIcon.setImageResource(attributes.getResourceId(R.styleable.UserCreateSpinner_spinner_icon, 0))
            ivIcon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
            ivIcon.layoutParams.height = attributes.getLayoutDimension(R.styleable.UserCreateSpinner_spinner_iconHeight, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.layoutParams.width = attributes.getLayoutDimension(R.styleable.UserCreateSpinner_spinner_iconWidth, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.visibility = if (attributes.getBoolean(R.styleable.UserCreateSpinner_spinner_showIcon, true)) View.VISIBLE else View.GONE
            attributes.recycle()
        }
    }

    fun getSpinner() = spSelectField
}