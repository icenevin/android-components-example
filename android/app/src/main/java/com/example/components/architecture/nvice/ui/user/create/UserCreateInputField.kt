package com.example.components.architecture.nvice.ui.user.create

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.components.architecture.nvice.R
import kotlinx.android.synthetic.main.view_user_create_input_field.view.*

class UserCreateInputField @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_user_create_input_field, this, true)
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.UserCreateInputField, 0, 0)
            val iconPadding = attributes.getLayoutDimension(R.styleable.UserCreateInputField_field_iconPadding, 0)
            ivIcon.setImageResource(attributes.getResourceId(R.styleable.UserCreateInputField_field_icon, 0))
            ivIcon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
            ivIcon.layoutParams.height = attributes.getLayoutDimension(R.styleable.UserCreateInputField_field_iconHeight, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.layoutParams.width = attributes.getLayoutDimension(R.styleable.UserCreateInputField_field_iconWidth, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.visibility = if (attributes.getBoolean(R.styleable.UserCreateInputField_field_showIcon, true)) View.VISIBLE else View.GONE
            edtInputField.hint = attributes.getString(R.styleable.UserCreateInputField_field_hint)
            edtInputField.setText(attributes.getString(R.styleable.UserCreateInputField_field_text))
            attributes.recycle()
        }
    }

    fun setText(text: String) {
        edtInputField.setText(text)
    }

    fun getText() = edtInputField.text

    fun setIconTint(@ColorRes color: Int) {
        DrawableCompat.setTint(DrawableCompat.wrap(ivIcon.drawable).mutate(), ContextCompat.getColor(context, color))
    }
}