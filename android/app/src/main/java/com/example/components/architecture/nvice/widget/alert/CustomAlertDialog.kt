package com.example.components.architecture.nvice.widget.alert

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.example.components.architecture.nvice.R
import kotlinx.android.synthetic.main.alert_dialog_default.*

class CustomAlertDialog constructor(
        context: Context
) : Dialog(context, R.style.AppDialog) {

    companion object {
        fun with(context: Context?) = context?.let {
            CustomAlertDialog(it)
        }
    }

    init {
        this.window?.setBackgroundDrawableResource(android.R.color.transparent)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setCancelable(false)
        this.setContentView(R.layout.alert_dialog_default)
        this.ivDrawable.visibility = View.GONE
        this.tvTitle.visibility = View.GONE
        this.tvSupportText.visibility = View.GONE
        this.btnNegative.visibility = View.GONE

        this.btnPositive.setOnClickListener { dismiss() }
        this.btnNegative.setOnClickListener { dismiss() }
    }

    override fun setTitle(title: CharSequence?) {
        this.tvTitle.text = title
        this.tvTitle.visibility = View.VISIBLE
    }

    override fun setTitle(titleId: Int) {
        this.tvTitle.text = context.getText(titleId)
        this.tvTitle.visibility = View.VISIBLE
    }

    fun setTitleText(text: Any): CustomAlertDialog {
        if (text is String) setTitle(text)
        if (text is @androidx.annotation.StringRes Int) setTitle(text)
        return this
    }

    fun setTitleTextColor(@ColorRes color: Int): CustomAlertDialog {
        this.tvTitle.setTextColor(ContextCompat.getColor(context, color))
        return this
    }

    fun setTitleText(text: Any, @ColorRes color: Int): CustomAlertDialog {
        return this.setTitleText(text)
                .setTitleTextColor(color)
    }

    fun setSupportingText(text: Any): CustomAlertDialog {
        this.tvSupportText.visibility = View.VISIBLE
        if (text is String) this.tvSupportText.text = text
        if (text is @androidx.annotation.StringRes Int) this.tvSupportText.text = context.getText(text)
        return this
    }

    fun setSupportingTextColor(@ColorRes color: Int): CustomAlertDialog {
        this.tvSupportText.setTextColor(ContextCompat.getColor(context, color))
        return this
    }

    fun setSupportingText(text: Any, @ColorRes color: Int): CustomAlertDialog {
        return this.setSupportingText(text)
                .setSupportingTextColor(color)
    }

    fun setDrawable(@DrawableRes drawable: Int): CustomAlertDialog {
        this.ivDrawable.setImageResource(drawable)
        return this
    }

    fun setDrawableTint(@ColorRes color: Int): CustomAlertDialog {
        this.ivDrawable.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.MULTIPLY)
        return this
    }

    fun setDrawable(@DrawableRes drawable: Int, @ColorRes color: Int): CustomAlertDialog {
        return this.setDrawable(drawable)
                .setDrawableTint(color)
    }

    fun setPositiveButton(text: Any): CustomAlertDialog {
        this.btnPositive.visibility = View.VISIBLE
        if (text is String) this.btnPositive.text = text
        if (text is @androidx.annotation.StringRes Int) this.btnPositive.text = context.getText(text)
        return this
    }

    fun setPositiveButton(event: CustomAlertDialog.(View) -> Unit): CustomAlertDialog {
        this.btnPositive.visibility = View.VISIBLE
        this.btnPositive.setOnClickListener {
            event(this, it)
            dismiss()
        }
        return this
    }

    fun setPositiveButton(text: Any, event: CustomAlertDialog.(View) -> Unit): CustomAlertDialog {
        return this.setPositiveButton(text)
                .setPositiveButton(event)
    }


    fun setNegativeButton(text: Any): CustomAlertDialog {
        this.btnNegative.visibility = View.VISIBLE
        if (text is String) this.btnNegative.text = text
        if (text is @androidx.annotation.StringRes Int) this.btnNegative.text = context.getText(text)
        return this
    }

    fun setNegativeButton(event: CustomAlertDialog.(View) -> Unit): CustomAlertDialog {
        this.btnNegative.visibility = View.VISIBLE
        this.btnNegative.setOnClickListener {
            event(this, it)
            dismiss()
        }
        return this
    }

    fun setNegativeButton(text: Any, event: CustomAlertDialog.(View) -> Unit): CustomAlertDialog {
        return this.setNegativeButton(text)
                .setNegativeButton(event)
    }
}
