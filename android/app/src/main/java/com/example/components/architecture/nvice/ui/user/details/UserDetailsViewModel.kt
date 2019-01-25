package com.example.components.architecture.nvice.ui.user.details

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.model.UserStatus
import javax.inject.Inject

class UserDetailsViewModel @Inject constructor() : ViewModel() {

    lateinit var user: User

    fun getFullName() = user.firstName + " " + user.lastName
    fun getPosition() = user.position?.getCapitalizedName()
}