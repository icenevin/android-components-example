package com.example.components.architecture.nvice.ui.user.create

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.components.architecture.nvice.widget.fields.CustomFieldSpinner
import android.widget.AdapterView
import androidx.databinding.InverseBindingListener
import android.view.View
import androidx.databinding.InverseBindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.components.architecture.nvice.model.UserPosition
import com.example.components.architecture.nvice.model.UserStatus
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.components.architecture.nvice.BaseViewModel
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.ui.LoadingStatus


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
                    .transforms(CenterCrop())
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
fun setLoader(view: SwipeRefreshLayout, status: LoadingStatus?) {
    status?.let {
        when (status) {
            LoadingStatus.FINISHED -> view.isRefreshing = false
            LoadingStatus.PROCESSING -> view.isRefreshing = true
            LoadingStatus.IDLE -> view.isRefreshing = false
        }
    }
}


@BindingAdapter("android:enable")
fun setLoaderEnable(view: SwipeRefreshLayout, enable: Boolean?) {
    view.isEnabled = enable ?: true
}