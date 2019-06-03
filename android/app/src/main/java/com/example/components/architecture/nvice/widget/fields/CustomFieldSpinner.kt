package com.example.components.architecture.nvice.widget.fields

import android.content.Context
import android.content.res.Resources
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.databinding.InverseBindingListener
import com.example.components.architecture.nvice.R
import kotlinx.android.synthetic.main.view_custom_field_spinner.view.*

open class CustomFieldSpinner @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : CustomField(context, attrs, defStyle) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_custom_field_spinner, this, true)
        initView(attrs)
    }

    override fun setOnClick(listener: OnClickListener) {
        spSelectField.setOnClickListener { v -> listener.onClick(v) }
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

    fun getSpinner() = spSelectField

    fun setSelection(position: Int) {
        spSelectField.setSelection(position)
    }

    fun <T> setAdapter(adapter: CustomFieldSpinnerAdapter<T>) {
        spSelectField.adapter = adapter
    }

    fun getAdapter() = spSelectField.adapter as CustomFieldSpinnerAdapter<*>

    private fun initView(attrs: AttributeSet?) {
        attrs?.let {
            val constraintSet = ConstraintSet()
            val attributes = context.obtainStyledAttributes(it, R.styleable.CustomFieldSpinner, 0, 0)
            val iconResource = attributes.getResourceId(R.styleable.CustomFieldSpinner_field_icon, 0)
            val fullWith = attributes.getBoolean(R.styleable.CustomFieldSpinner_field_fullWidth, false)
            val showDrawableEnd = attributes.getBoolean(R.styleable.CustomFieldSpinner_field_showDrawableEnd, false)
            val drawableEndPadding = attributes.getLayoutDimension(R.styleable.CustomFieldEditText_field_drawableEndPadding, 0)
            val iconPadding = attributes.getLayoutDimension(R.styleable.CustomFieldSpinner_field_iconPadding, 0)

            constraintSet.clone(cstContainer)
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

            if (iconResource != 0) {
                val marginLayoutParams = spSelectField.layoutParams as MarginLayoutParams
                marginLayoutParams.marginStart = context.resources.getDimensionPixelSize(R.dimen.dp_8)
            }

            spSelectField.isEnabled = attributes.getBoolean(R.styleable.CustomFieldSpinner_field_enable, true)

            ivDrawableEnd.setImageResource(attributes.getResourceId(R.styleable.CustomFieldEditText_field_drawableEnd, 0))
            ivDrawableEnd.setPadding(drawableEndPadding, drawableEndPadding, drawableEndPadding, drawableEndPadding)
            ivDrawableEnd.visibility = if (attributes.getBoolean(R.styleable.CustomFieldEditText_field_showDrawableEnd, false)) View.VISIBLE else View.GONE

            ivIcon.setImageResource(attributes.getResourceId(R.styleable.CustomFieldSpinner_field_icon, 0))
            ivIcon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
            ivIcon.layoutParams.height = attributes.getLayoutDimension(R.styleable.CustomFieldSpinner_field_iconHeight, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.layoutParams.width = attributes.getLayoutDimension(R.styleable.CustomFieldSpinner_field_iconWidth, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.visibility = if (attributes.getBoolean(R.styleable.CustomFieldSpinner_field_showIcon, true)) View.VISIBLE else View.GONE

            spSelectField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    view?.let { v ->
                        with(v) {
                            setPadding(0, paddingTop, paddingRight, paddingBottom)
                        }
                    }
                }
            }
            attributes.recycle()
        }
    }
}