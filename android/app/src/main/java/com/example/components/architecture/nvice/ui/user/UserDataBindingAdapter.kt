package com.example.components.architecture.nvice.ui.user

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.components.architecture.nvice.model.User

@Suppress("unused")
@BindingAdapter("bind:item_userAvatar")
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