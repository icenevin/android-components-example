package com.example.components.architecture.nvice.ui.user.profile

import android.content.Context
import android.graphics.PorterDuff
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.components.architecture.nvice.databinding.ViewFieldUserSkillBinding
import kotlinx.android.synthetic.main.view_field_user_skill.view.*


class UserSkillField @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var binding: ViewFieldUserSkillBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewFieldUserSkillBinding.inflate(inflater, this, true)

        binding.sbScoreBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvScoreValue.text = progress.toString()
                binding.skill?.let {
                    it.score = progress
                    sbScoreBar.progressDrawable.setColorFilter(it.getScoreRatingColor(), PorterDuff.Mode.MULTIPLY)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.edtSkill.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.skill?.name = s.toString()
                if (!s.isNullOrEmpty()) {
                    binding.viewModel?.includeEmptySkill()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    fun bind(viewModel: UserProfileViewModel, position: Int) {
        val skill = viewModel.skills[position]
        binding.viewModel = viewModel
        skill?.let {
            binding.skill = it
            sbScoreBar.progressDrawable.setColorFilter(it.getScoreRatingColor(), PorterDuff.Mode.MULTIPLY)
        }
    }
}