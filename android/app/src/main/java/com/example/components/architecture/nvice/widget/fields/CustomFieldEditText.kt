package com.example.components.architecture.nvice.widget.fields

import android.content.Context
import android.content.res.Resources
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
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
        defStyle: Int = 0
) : CustomField(context, attrs, defStyle) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_custom_field_edittext, this, true)
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.CustomFieldEditText, 0, 0)
            val iconPadding = attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_iconPadding, 0)
            val inputDrawableEndPadding = attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_inputDrawableEndPadding, 0)
            val drawableEndPadding = attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_drawableEndPadding, 0)
            val showInputDrawableEnd = attributes.getBoolean(R.styleable.CustomFieldEditText_field_showInputDrawableEnd, false)

            ivIcon.setImageResource(attributes.getResourceId(R.styleable.CustomFieldEditText_field_icon, 0))
            ivIcon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
            ivIcon.layoutParams.height = attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_iconHeight, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.layoutParams.width = attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_iconWidth, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.visibility = if (attributes.getBoolean(R.styleable.CustomFieldEditText_field_showIcon, true)) View.VISIBLE else View.GONE

            ivInputDrawableEnd.setImageResource(attributes.getResourceId(R.styleable.CustomFieldEditText_field_inputDrawableEnd, 0))
            ivInputDrawableEnd.setPadding(inputDrawableEndPadding, inputDrawableEndPadding, inputDrawableEndPadding, inputDrawableEndPadding)
            ivInputDrawableEnd.visibility = if (showInputDrawableEnd) View.VISIBLE else View.GONE

            ivDrawableEnd.setImageResource(attributes.getResourceId(R.styleable.CustomFieldEditText_field_drawableEnd, 0))
            ivDrawableEnd.setPadding(drawableEndPadding, drawableEndPadding, drawableEndPadding, drawableEndPadding)
            ivDrawableEnd.visibility = if (attributes.getBoolean(R.styleable.CustomFieldEditText_field_showDrawableEnd, false)) View.VISIBLE else View.GONE

            edtInputField.hint = attributes.getString(R.styleable.CustomFieldEditText_field_hint)
            edtInputField.isEnabled = attributes.getBoolean(R.styleable.CustomFieldEditText_field_enable, true)
            edtInputField.isFocusable = attributes.getBoolean(R.styleable.CustomFieldEditText_field_editable, true)
            edtInputField.isLongClickable = attributes.getBoolean(R.styleable.CustomFieldEditText_field_longClickable, true)
            edtInputField.maxLines = attributes.getInteger(R.styleable.CustomFieldEditText_field_maxLines, 0)
            edtInputField.setPadding(
                    attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_paddingStart, edtInputField.paddingStart),
                    attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_paddingTop, edtInputField.paddingTop),
                    if (showInputDrawableEnd) resources.getDimension(R.dimen.dp_36).toInt() else attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_paddingEnd, edtInputField.paddingEnd),
                    attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_paddingBottom, edtInputField.paddingBottom)
            )

            edtInputField.setSingleLine(attributes.getBoolean(R.styleable.CustomFieldEditText_field_singleLines, false))
            edtInputField.setText(attributes.getString(R.styleable.CustomFieldEditText_field_text))

            attributes.recycle()
        }
    }

    override fun setOnClick(listener: OnClickListener) {
        edtInputField.setOnClickListener { v -> listener.onClick(v) }
    }

    override fun setOnDrawableEndClick(listener: OnClickListener) {
        ivDrawableEnd.setOnClickListener { v -> listener.onClick(v) }
    }

    override fun setIconTint(@ColorRes color: Int) {
        try {
            DrawableCompat.setTint(DrawableCompat.wrap(ivIcon.drawable).mutate(), ContextCompat.getColor(context, color))
        } catch (e: Resources.NotFoundException) {
        }
    }

    override fun setDrawableEndTint(color: Int) {
        try {
            DrawableCompat.setTint(DrawableCompat.wrap(ivDrawableEnd.drawable).mutate(), ContextCompat.getColor(context, color))
        } catch (e: Resources.NotFoundException) {
        }
    }

    fun setText(text: String) {
        edtInputField.setText(text)
    }

    fun getText() = edtInputField.text ?: null

    fun setError(text: String) {
        edtInputField.error = text
    }

    fun setError(error: CustomFieldError?){
        error?.let {
            edtInputField.setError(it.text, it.drawable)
        }
    }

    fun addTextChangedListener(watcher: TextWatcher) {
        edtInputField.addTextChangedListener(watcher)
    }

    fun setOnInputDrawableEndClick(listener: OnClickListener) {
        ivInputDrawableEnd.setOnClickListener { v -> listener.onClick(v) }
    }

    fun setInputDrawableEndTint(color: Int) {
        try {
            DrawableCompat.setTint(DrawableCompat.wrap(ivInputDrawableEnd.drawable).mutate(), ContextCompat.getColor(context, color))
        } catch (e: Resources.NotFoundException) {
        }
    }

    fun setShowInputDrawableEnd(requestShow: Boolean) {
        ivInputDrawableEnd.visibility = if (requestShow) View.VISIBLE else View.GONE
        edtInputField.setPadding(
                edtInputField.paddingStart,
                edtInputField.paddingTop,
                if (requestShow) resources.getDimension(R.dimen.dp_36).toInt() else edtInputField.paddingEnd,
                edtInputField.paddingBottom
        )
    }
}