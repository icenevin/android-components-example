package com.example.components.architecture.nvice.ui.user

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.google.android.material.chip.Chip
import androidx.recyclerview.widget.AsyncListDiffer
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.databinding.ItemUserStatusFilterBinding
import com.example.components.architecture.nvice.model.UserStatus
import androidx.recyclerview.widget.DiffUtil


class UserStatusListAdapter : RecyclerView.Adapter<UserStatusListAdapter.UserStatusViewHolder>() {

    private val mDiffer = AsyncListDiffer(this, DIFF_CALLBACK)
    lateinit var onCheckedChangeListener: OnCheckedChangeListener

    fun submitList(statusList: List<UserStatus>) {
        mDiffer.submitList(statusList)
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserStatusListAdapter.UserStatusViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemUserStatusFilterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_user_status_filter, parent, false)
        return UserStatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserStatusListAdapter.UserStatusViewHolder, position: Int) {
        holder.itemView as Chip
        holder.bind(mDiffer.currentList[position])
    }

    inner class UserStatusViewHolder(private val binding: ItemUserStatusFilterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(filter: UserStatus) = with(itemView as Chip) {
            binding.userStatus = filter
            setOnCheckedChangeListener { _, isChecked ->
                onCheckedChangeListener.onCheckedChangeListener(isChecked, filter)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserStatus>() {
            override fun areItemsTheSame(oldUserStatus: UserStatus, newUserStatus: UserStatus): Boolean = oldUserStatus.id == newUserStatus.id
            override fun areContentsTheSame(oldUserStatus: UserStatus, newUserStatus: UserStatus): Boolean = oldUserStatus == newUserStatus
        }
    }

    interface OnCheckedChangeListener {
        fun onCheckedChangeListener(isChecked: Boolean, userStatus: UserStatus)
    }
}