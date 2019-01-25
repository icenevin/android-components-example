package com.example.components.architecture.nvice.ui.user.create

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.support.annotation.ColorRes

@Suppress("unused")
@SuppressLint("ResourceAsColor")
@BindingAdapter("app:field_iconTint")
fun setColorTint(view: UserCreateInputField, @ColorRes color: Int) {
    view.setIconTint(color)
}

@Suppress("unused")
@BindingAdapter("app:field_text")
fun setInfoLabel(view: UserCreateInputField, label: String) {
    view.setText(label)
}