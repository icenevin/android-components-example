package com.example.components.architecture.nvice.ui.user.details

import android.graphics.PorterDuff
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.databinding.ItemUserSkillBinding
import com.example.components.architecture.nvice.model.user.UserSkill
import kotlinx.android.synthetic.main.item_user_skill.view.*


class UserSkillListAdapter : RecyclerView.Adapter<UserSkillListAdapter.UserSkillViewHolder>() {

    private val mDiffer = AsyncListDiffer(this, DiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSkillViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemUserSkillBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_user_skill, parent, false)
        return UserSkillViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserSkillViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(list: List<UserSkill?>?) {
        mDiffer.submitList(list)
    }

    inner class UserSkillViewHolder(private val binding: ItemUserSkillBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(skill: UserSkill) = with(itemView) {
            binding.skill = skill
            pbScoreBar.progressDrawable.setColorFilter(skill.getScoreRatingColor(), PorterDuff.Mode.MULTIPLY)
        }
    }

    object DiffCallback: DiffUtil.ItemCallback<UserSkill>() {
        override fun areItemsTheSame(oldItem: UserSkill, newItem: UserSkill): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserSkill, newItem: UserSkill): Boolean {
            return oldItem == newItem
        }
    }
}