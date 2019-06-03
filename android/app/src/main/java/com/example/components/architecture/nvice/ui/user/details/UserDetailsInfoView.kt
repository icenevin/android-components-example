package com.example.components.architecture.nvice.ui.user.details

import android.content.Context
import android.content.res.Resources
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.components.architecture.nvice.R
import kotlinx.android.synthetic.main.view_user_details_info.view.*


class UserDetailsInfoView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_user_details_info, this, true)
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.UserDetailsInfoView, 0, 0)
            val iconPadding = attributes.getLayoutDimension(R.styleable.UserDetailsInfoView_info_iconPadding, 0)
            val text = attributes.getString(R.styleable.UserDetailsInfoView_info_label)
            ivIcon.setImageResource(attributes.getResourceId(R.styleable.UserDetailsInfoView_info_icon, 0))
            ivIcon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
            ivIcon.layoutParams.height = attributes.getLayoutDimension(R.styleable.UserDetailsInfoView_info_iconHeight, resources.getDimension(R.dimen.dp_24).toInt())
            ivIcon.layoutParams.width = attributes.getLayoutDimension(R.styleable.UserDetailsInfoView_info_iconWidth, resources.getDimension(R.dimen.dp_24).toInt())
            tvLabel.text = text
            setVisibility(text)
            attributes.recycle()
        }
    }

    fun setLabel(label: String) {
        tvLabel.text = label
        setVisibility(label)
    }

    fun setIconTint(@ColorRes color: Int) {
        try {
            DrawableCompat.setTint(DrawableCompat.wrap(ivIcon.drawable).mutate(), ContextCompat.getColor(context, color))
        } catch (e: Resources.NotFoundException) {

        }
    }

    private fun setVisibility(text: String?) {
        visibility = if (text?.isNotEmpty() == true) View.VISIBLE else View.GONE
    }
}