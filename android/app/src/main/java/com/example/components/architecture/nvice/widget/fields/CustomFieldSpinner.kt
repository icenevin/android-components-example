package com.example.components.architecture.nvice.widget.fields

import android.content.Context
import android.content.res.Resources
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.components.architecture.nvice.R
import kotlinx.android.synthetic.main.view_custom_field_spinner.view.*

class CustomFieldSpinner @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : CustomField(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_custom_field_spinner, this, true)
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.CustomFieldSpinner, 0, 0)
            val fullWith = attributes.getBoolean(R.styleable.CustomFieldSpinner_field_fullWidth, false)
            spSelectField.layoutParams = LinearLayout.LayoutParams(spSelectField.width, LayoutParams.MATCH_PARENT, if (fullWith) 2f else 1f)
            val iconPadding = attributes.getLayoutDimension(R.styleable.CustomFieldSpinner_field_iconPadding, 0)
            ivIcon.setImageResource(attributes.getResourceId(R.styleable.CustomFieldSpinner_field_icon, 0))
            ivIcon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
            ivIcon.layoutParams.height = attributes.getLayoutDimension(R.styleable.CustomFieldSpinner_field_iconHeight, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.layoutParams.width = attributes.getLayoutDimension(R.styleable.CustomFieldSpinner_field_iconWidth, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.visibility = if (attributes.getBoolean(R.styleable.CustomFieldSpinner_field_showIcon, true)) View.VISIBLE else View.GONE
            attributes.recycle()
        }
    }

    fun getSpinner() = spSelectField

    override fun setIconTint(@ColorRes color: Int) {
        try {
            DrawableCompat.setTint(DrawableCompat.wrap(ivIcon.drawable).mutate(), ContextCompat.getColor(context, color))
        } catch (e: Resources.NotFoundException) {
        }
    }
}