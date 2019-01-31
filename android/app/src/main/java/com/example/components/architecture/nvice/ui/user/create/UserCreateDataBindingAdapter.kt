package com.example.components.architecture.nvice.ui.user.create

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.components.architecture.nvice.widget.fields.CustomFieldSpinner
import android.widget.AdapterView
import android.databinding.InverseBindingListener
import android.view.View
import android.databinding.InverseBindingAdapter
import android.support.v4.widget.SwipeRefreshLayout
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.components.architecture.nvice.model.UserPosition
import com.example.components.architecture.nvice.model.UserStatus
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.util.DimensUtil


@Suppress("unused")
@BindingAdapter("bind:field_avatar")
fun setAvatar(view: ImageView, value: String?) {

    Glide.with(view.context)
            .load(value)
            .apply(RequestOptions.circleCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .into(view)
}

@Suppress("unused")
@BindingAdapter("bind:field_cover")
fun setCover(view: ImageView, value: String?) {

    Glide.with(view.context)
            .load(value)
            .apply(RequestOptions()
                    .transforms(CenterCrop(), RoundedCorners(view.context.resources.getDimension(R.dimen.dp_16).toInt()))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true))
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .into(view)
}

@Suppress("unused")
@BindingAdapter("bind:selectedUserPosition")
fun setSelectedUserPosition(view: CustomFieldSpinner, value: UserPosition?) {
    value?.let {
        view.getSpinner().setSelection(it.ordinal)
    }
}

@Suppress("unused")
@InverseBindingAdapter(attribute = "bind:selectedUserPosition")
fun getSelectedUserPosition(view: CustomFieldSpinner): UserPosition {
    return view.getSpinner().selectedItem as UserPosition
}

@Suppress("unused")
@BindingAdapter("bind:selectedUserPositionAttrChanged")
fun setOnUserPositionChangeListener(
        view: CustomFieldSpinner,
        attrChange: InverseBindingListener
) {
    view.getSpinner().onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            attrChange.onChange()
        }
    }
}

@Suppress("unused")
@BindingAdapter(value = ["bind:selectedUserStatus", "bind:selectedUserStatusAttrChanged"], requireAll = false)
fun setSelectedUserStatus(view: CustomFieldSpinner, value: UserStatus?, attrChange: InverseBindingListener) {

    view.getSpinner().onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            attrChange.onChange()
        }
    }

    value?.let {
        view.getSpinner().setSelection(it.ordinal)
    }
}

@Suppress("unused")
@InverseBindingAdapter(attribute = "bind:selectedUserStatus")
fun getSelectedUserStatus(view: CustomFieldSpinner): UserStatus {
    return view.getSpinner().selectedItem as UserStatus
}

@BindingAdapter("bind:loading")
fun setLoader(view: SwipeRefreshLayout, status: UserCreateViewModel.LoadingStatus?) {
    status?.let {
        when (status) {
            UserCreateViewModel.LoadingStatus.FINISHED -> view.isRefreshing = false
            UserCreateViewModel.LoadingStatus.PROCESSING -> view.isRefreshing = true
            UserCreateViewModel.LoadingStatus.IDLE -> view.isRefreshing = false
        }
    }
}


@BindingAdapter("android:enable")
fun setLoaderEnable(view: SwipeRefreshLayout, enable: Boolean?) {
    view.isEnabled = enable ?: true
}