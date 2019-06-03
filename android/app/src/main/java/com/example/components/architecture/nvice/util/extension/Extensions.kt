package com.example.components.architecture.nvice.util.extension

import androidx.lifecycle.MutableLiveData
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import java.util.*


fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) +  start

fun AppCompatImageView.load(url: String){
    Glide.with(context).load(url).into(this)
}

fun <T : Any?> MutableLiveData<T>.init(value: T?) = apply { setValue(value) }

fun <T : Any?> MutableLiveData<T>.updateSelf() = apply { setValue(this.value) }