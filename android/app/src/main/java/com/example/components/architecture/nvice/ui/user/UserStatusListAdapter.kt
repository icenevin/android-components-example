package com.example.components.architecture.nvice.ui.user

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding


class UserStatusListAdapter constructor(private val viewModel: UserViewModel) : RecyclerView.Adapter<UserStatusListAdapter.UserStatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserStatusListAdapter.UserStatusViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return UserStatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserStatusListAdapter.UserStatusViewHolder, position: Int) {
        holder.bind(viewModel, position)
    }

    inner class UserStatusViewHolder (private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: UserViewModel, variableId: Int) {
            binding.setVariable(variableId, viewModel)
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return viewModel.getUserStatus().value?.size?.let { it } ?: 0
    }
}