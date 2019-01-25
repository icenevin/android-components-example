package com.example.components.architecture.nvice.ui.user.details

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.support.annotation.ColorRes
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.components.architecture.nvice.model.User

@Suppress("unused")
@BindingAdapter("bind:userAvatar")
fun setAvatar(view: ImageView, user: User) {
    if (user.avatar != null) {
        user.avatar.let {
            Glide.with(view.context)
                    .load(it)
                    .apply( RequestOptions.circleCropTransform().error(user.status?.getColorResource()!!))
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    .into(view)
        }
    } else {
        view.setImageResource(user.status?.getColorResource()!!)
    }
}

@Suppress("unused")
@BindingAdapter("bind:userBackground")
fun setUserBackground(view: View, color: Int) {
    view.setBackgroundResource(color)
}

@Suppress("unused")
@SuppressLint("ResourceAsColor")
@BindingAdapter("app:info_iconTint")
fun setColorTint(view: UserDetailsInfoView, @ColorRes color: Int) {
    view.setIconTint(color)
}

@Suppress("unused")
@BindingAdapter("app:info_label")
fun setInfoLabel(view: UserDetailsInfoView, label: String) {
    view.setLabel(label)
}
