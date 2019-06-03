package com.example.components.architecture.nvice.ui.user.profile

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.databinding.ItemUserExperienceBinding
import com.example.components.architecture.nvice.model.user.UserExperience
import kotlinx.android.synthetic.main.item_user_experience.view.*


class UserExperienceListAdapter : RecyclerView.Adapter<UserExperienceListAdapter.UserExperienceViewHolder>() {

    private val mDiffer = AsyncListDiffer(this, DiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserExperienceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemUserExperienceBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_user_experience, parent, false)
        return UserExperienceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserExperienceViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(list: List<UserExperience?>?) {
        mDiffer.submitList(list)
    }

    inner class UserExperienceViewHolder(private val binding: ItemUserExperienceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(experience: UserExperience) = with(itemView) {
            binding.experience = experience
            if (experience == mDiffer.currentList.last()) {
                val constraintSet = ConstraintSet()
                constraintSet.clone(itemView as ConstraintLayout)
                constraintSet.clear(vLine.id, ConstraintSet.BOTTOM)
                constraintSet.connect(vLine.id, ConstraintSet.BOTTOM, vLinePoint.id, ConstraintSet.BOTTOM)
                constraintSet.applyTo(itemView)
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<UserExperience>() {
        override fun areItemsTheSame(oldItem: UserExperience, newItem: UserExperience): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserExperience, newItem: UserExperience): Boolean {
            return oldItem == newItem
        }
    }
}