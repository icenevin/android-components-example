package com.example.components.architecture.nvice.ui.user.details

import android.annotation.SuppressLint
import android.content.res.Resources
import android.databinding.BindingAdapter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.ColorUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.util.glide.AlwaysCrossFadeTransitionFactory

@Suppress("unused")
@SuppressLint("ResourceAsColor")
@BindingAdapter(value = ["bind:info_avatar", "bind:info_avatarErrorColor"], requireAll = false)
fun setAvatar(view: ImageView, avatar: String?, @ColorRes color: Int?) {

    Glide.with(view.context)
            .load(avatar)
            .apply(RequestOptions.circleCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .into(view)

    if (avatar.isNullOrEmpty()) {
        view.backgroundTintList = ContextCompat.getColorStateList(view.context, color
                ?: R.color.item_user_avatar_color)
    }
}

@Suppress("unused")
@BindingAdapter("bind:info_cover")
fun setCover(view: ImageView, avatar: String?) {
    avatar?.let {
        Glide.with(view.context)
                .load(avatar)
                .apply(RequestOptions.centerCropTransform().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .transition(DrawableTransitionOptions.with(AlwaysCrossFadeTransitionFactory(200)))
                .into(view)
    }
}

@Suppress("unused")
@BindingAdapter(value = ["bind:info_backgroundColor", "bind:info_backgroundColorAlpha"], requireAll = false)
fun setUserBackgroundColor(view: View, @ColorRes color: Int?, alpha: Int?) {

    val mAlpha = alpha?.let {
        if (it !in 0..255) {
            if (it > 255) 255 else 0
        } else it
    } ?: 255

    color?.let {
        view.setBackgroundColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(view.context, it), mAlpha))
    }
}

@Suppress("unused")
@BindingAdapter("bind:info_background")
fun setUserBackgroundColor(view: View, drawable: Drawable) {
    view.background = drawable
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
