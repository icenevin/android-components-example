package com.example.components.architecture.nvice.widget.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.databinding.BottomSheetUserAvatarPickerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserAvatarMenuModal constructor(
        private var randomEvent: (UserAvatarMenuModal.() -> Unit)? = null,
        private var categoryEvent: (UserAvatarMenuModal.() -> Unit)? = null
) : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(
                randomEvent: (UserAvatarMenuModal.() -> Unit)?,
                chooseCategoryEvent: (UserAvatarMenuModal.() -> Unit)?
        ) = UserAvatarMenuModal(randomEvent, chooseCategoryEvent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: BottomSheetUserAvatarPickerBinding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_user_avatar_picker, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        return binding.root
    }

    fun show(fm: FragmentManager) {
        super.show(fm, this::class.java.name)
    }

    fun randomAvatar() {
        randomEvent?.invoke(this)
    }

    fun chooseCategory() {
        categoryEvent?.invoke(this)
    }
}