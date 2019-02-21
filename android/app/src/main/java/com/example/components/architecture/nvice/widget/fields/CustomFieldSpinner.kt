package com.example.components.architecture.nvice.widget.fields

import android.content.Context
import android.content.res.Resources
import android.support.annotation.ColorRes
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.example.components.architecture.nvice.R
import kotlinx.android.synthetic.main.view_custom_field_spinner.view.*

class CustomFieldSpinner @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : CustomField(context, attrs, defStyle) {


    init {
        LayoutInflater.from(context).inflate(R.layout.view_custom_field_spinner, this, true)
        attrs?.let {

            val constraintSet = ConstraintSet()
            constraintSet.clone(cstContainer)
            val attributes = context.obtainStyledAttributes(it, R.styleable.CustomFieldSpinner, 0, 0)
            val fullWith = attributes.getBoolean(R.styleable.CustomFieldSpinner_field_fullWidth, false)
            val showDrawableEnd = attributes.getBoolean(R.styleable.CustomFieldSpinner_field_showDrawableEnd, false)
            val drawableEndPadding = attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_drawableEndPadding, 0)
            val iconPadding = attributes.getLayoutDimension(R.styleable.CustomFieldSpinner_field_iconPadding, 0)

            constraintSet.clear(spSelectField.id, ConstraintSet.END)
            if (fullWith) {
                if (showDrawableEnd) {
                    constraintSet.connect(spSelectField.id, ConstraintSet.END, endGuideline.id, ConstraintSet.START, 0)
                } else {
                    constraintSet.connect(spSelectField.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                }
            } else {
                constraintSet.connect(spSelectField.id, ConstraintSet.END, centerGuideline.id, ConstraintSet.START, 0)
            }
            constraintSet.applyTo(cstContainer)

            spSelectField.isEnabled = attributes.getBoolean(R.styleable.CustomFieldSpinner_field_enable, true)

            ivDrawableEnd.setImageResource(attributes.getResourceId(R.styleable.CustomFieldEditText_field_drawableEnd, 0))
            ivDrawableEnd.setPadding(drawableEndPadding, drawableEndPadding, drawableEndPadding, drawableEndPadding)
            ivDrawableEnd.visibility = if (attributes.getBoolean(R.styleable.CustomFieldEditText_field_showDrawableEnd, false)) View.VISIBLE else View.GONE

            ivIcon.setImageResource(attributes.getResourceId(R.styleable.CustomFieldSpinner_field_icon, 0))
            ivIcon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
            ivIcon.layoutParams.height = attributes.getLayoutDimension(R.styleable.CustomFieldSpinner_field_iconHeight, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.layoutParams.width = attributes.getLayoutDimension(R.styleable.CustomFieldSpinner_field_iconWidth, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.visibility = if (attributes.getBoolean(R.styleable.CustomFieldSpinner_field_showIcon, true)) View.VISIBLE else View.GONE

            attributes.recycle()
        }
    }

    fun getSpinner() = spSelectField

    override fun setOnClick(listener: OnClickListener) {
        spSelectField.setOnClickListener { v -> listener.onClick(v) }
    }

    override fun setOnDrawableEndClick(listener: OnClickListener) {
        ivDrawableEnd.setOnClickListener {  v -> listener.onClick(v)  }
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
}