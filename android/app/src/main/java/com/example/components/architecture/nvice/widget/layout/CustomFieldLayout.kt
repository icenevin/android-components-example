package com.example.components.architecture.nvice.widget.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.widget.fields.CustomField
import com.example.components.architecture.nvice.widget.fields.CustomFieldEditText
import com.example.components.architecture.nvice.widget.fields.CustomFieldSpinner


class CustomFieldLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var ivIcon: AppCompatImageView? = null

    private val constraintSet = ConstraintSet()

    init {
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.CustomFieldLayout, 0, 0)
            val iconResource = attributes.getResourceId(R.styleable.CustomFieldLayout_icon, 0)
            minHeight = context.resources.getDimensionPixelSize(R.dimen.custom_field_min_height)

            if (iconResource != 0) {
                ivIcon = AppCompatImageView(context).apply {
                    setImageResource(iconResource)
                    DrawableCompat.setTint(DrawableCompat.wrap(drawable).mutate(), ContextCompat.getColor(context, R.color.black_600))
                }.also { view ->
                    addView(view)
                    constraintSet.clone(this)
                    constraintSet.connect(view.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                    constraintSet.connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, context.resources.getDimensionPixelSize(R.dimen.dp_16))
                    constraintSet.applyTo(this)
                }
            }
            attributes.recycle()
        }
    }

    override fun onViewAdded(view: View?) {
        view?.let { v ->
            if (v.id == View.NO_ID) {
                v.id = View.generateViewId()
            }

            ivIcon?.let { icon ->
                if (v.id != icon.id) {
                    val viewConstraints = v.layoutParams as LayoutParams
                    constraintSet.clone(this)
                    constraintSet.clear(v.id, ConstraintSet.START)
                    constraintSet.connect(v.id, ConstraintSet.START, icon.id, ConstraintSet.END, context.resources.getDimensionPixelSize(R.dimen.dp_8))

                    if (viewConstraints.endToEnd == -1) {
                        constraintSet.connect(v.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                        val upperField = getChildAt(childCount - 2)
                        if (upperField.id != icon.id) {
                            constraintSet.connect(v.id, ConstraintSet.TOP, upperField.id, ConstraintSet.BOTTOM)
                        }
                    }

                    val iconConstraints = icon.layoutParams as LayoutParams
                    if (iconConstraints.bottomToBottom == -1) {
                        if (v is CustomField) {
                            constraintSet.setMargin(icon.id, 3, 0)
                            constraintSet.connect(icon.id, ConstraintSet.BOTTOM, v.id, ConstraintSet.BOTTOM)
                        }
                    }
                    constraintSet.applyTo(this)
                }
            }
        }
        super.onViewAdded(view)
    }
}