package com.example.components.architecture.nvice.util.glide

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.transition.*


class AlwaysCrossFadeTransitionFactory(val duration: Int) : TransitionFactory<Drawable> {
    override fun build(dataSource: DataSource?, isFirstResource: Boolean): Transition<Drawable>? {
        return DrawableCrossFadeTransition(duration, true)
    }
}