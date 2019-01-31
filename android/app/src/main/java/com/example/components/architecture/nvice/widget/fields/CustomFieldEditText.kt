package com.example.components.architecture.nvice.widget.fields

import android.content.Context
import android.content.res.Resources
import android.databinding.BindingMethod
import android.databinding.BindingMethods
import android.databinding.InverseBindingMethod
import android.databinding.InverseBindingMethods
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.example.components.architecture.nvice.R
import kotlinx.android.synthetic.main.view_custom_field_edittext.view.*


class CustomFieldEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : CustomField(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_custom_field_edittext, this, true)
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.CustomFieldEditText, 0, 0)
            val iconPadding = attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_iconPadding, 0)
            ivIcon.setImageResource(attributes.getResourceId(R.styleable.CustomFieldEditText_field_icon, 0))
            ivIcon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
            ivIcon.layoutParams.height = attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_iconHeight, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.layoutParams.width = attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_iconWidth, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.visibility = if (attributes.getBoolean(R.styleable.CustomFieldEditText_field_showIcon, true)) View.VISIBLE else View.GONE
            edtInputField.hint = attributes.getString(R.styleable.CustomFieldEditText_field_hint)
            edtInputField.maxLines = attributes.getInteger(R.styleable.CustomFieldEditText_field_maxLines, 0)
            edtInputField.setSingleLine(attributes.getBoolean(R.styleable.CustomFieldEditText_field_singleLines, false))
            edtInputField.setText(attributes.getString(R.styleable.CustomFieldEditText_field_text))
            attributes.recycle()
        }
    }

    fun setText(text: String) {
        edtInputField.setText(text)
    }

    fun getText() = edtInputField.text

    fun addTextChangedListener(watcher: TextWatcher){
        edtInputField.addTextChangedListener(watcher)
    }

    override fun setIconTint(@ColorRes color: Int) {
        try {
            DrawableCompat.setTint(DrawableCompat.wrap(ivIcon.drawable).mutate(), ContextCompat.getColor(context, color))
        } catch (e: Resources.NotFoundException) {
        }
    }
}