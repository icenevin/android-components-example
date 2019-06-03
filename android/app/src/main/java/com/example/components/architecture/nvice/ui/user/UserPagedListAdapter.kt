package com.example.components.architecture.nvice.ui.user

import androidx.paging.PagedListAdapter
import androidx.databinding.DataBindingUtil
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.databinding.ItemUserBinding
import com.example.components.architecture.nvice.model.user.User
import com.example.components.architecture.nvice.model.user.UserStatus
import kotlinx.android.synthetic.main.item_user.view.*

class UserPagedListAdapter : PagedListAdapter<User, UserPagedListAdapter.UserViewHolder>(DiffCallback), UserItemTouchHelperCallback.OnItemTouchListener {

    private lateinit var onClick: (View, User) -> Unit
    private lateinit var onDelete: (User) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemUserBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_user, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = holder.bind(getItem(position)!!, onClick, onDelete)

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var user: User
        fun bind(item: User, onClick: (View, User) -> Unit, onDelete: (User) -> Unit) = with(itemView) {
            binding.user = item
            user = item
            tvUserStatusName.generateTextColorByStatus(item.status)
            btnDelete.setOnClickListener { onDelete(item) }
            setOnClickListener { onClick(itemView, item) }
        }
    }

    fun setOnClickListener(onClick: (View, User) -> Unit) {
        this.onClick = onClick
    }

    fun setOnDeleteListener(onDelete: (User) -> Unit) {
        this.onDelete = onDelete
    }

    override fun onSwiped(holder: RecyclerView.ViewHolder) {
        this.onDelete((holder as UserViewHolder).user)
    }

    override fun onSwipedLeft(holder: RecyclerView.ViewHolder) {
    }

    override fun onSwipedRight(holder: RecyclerView.ViewHolder) {
    }

    object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}

fun ImageView.generateAvatarColorByStatus(status: UserStatus?) {
    status?.let {
        setImageResource(status.getColorResource())
    }
}

fun TextView.generateTextColorByStatus(status: UserStatus?) {
    status?.let {
        setTextColor(ContextCompat.getColor(context, status.getColorResource()))
    }
}