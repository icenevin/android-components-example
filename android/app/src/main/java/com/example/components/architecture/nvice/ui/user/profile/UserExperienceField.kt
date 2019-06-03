package com.example.components.architecture.nvice.ui.user.profile

import android.content.Context
import android.graphics.PorterDuff
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.components.architecture.nvice.databinding.ViewFieldUserExperienceBinding
import com.example.components.architecture.nvice.databinding.ViewFieldUserSkillBinding
import kotlinx.android.synthetic.main.view_field_user_skill.view.*


class UserExperienceField @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var binding: ViewFieldUserExperienceBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewFieldUserExperienceBinding.inflate(inflater, this, true)

        binding.edtExperience.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.experience?.work = s.toString()
                if (!s.isNullOrEmpty()) {
                    binding.viewModel?.includeEmptyExperience()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    fun bind(viewModel: UserProfileViewModel, position: Int) {
        val experience = viewModel.experiences[position]
        binding.viewModel = viewModel
        experience?.let {
            binding.experience = it
        }
    }
}