package com.example.components.architecture.nvice.ui.user

import androidx.databinding.BindingAdapter
import android.graphics.PorterDuff
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.model.User

@Suppress("unused")
@BindingAdapter(value = ["bind:item_avatar", "bind:item_avatarErrorColor"], requireAll = false)
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