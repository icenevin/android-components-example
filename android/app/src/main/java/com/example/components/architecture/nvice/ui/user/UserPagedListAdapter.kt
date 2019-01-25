package com.example.components.architecture.nvice.ui.user

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.databinding.ItemUserBinding
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.model.UserStatus
import kotlinx.android.synthetic.main.item_user.view.*

class UserPagedListAdapter(private val context: Context) : PagedListAdapter<User, UserPagedListAdapter.UserViewHolder>(User.DIFF_CALLBACK), UserItemTouchHelperCallback.OnItemTouchListener {

    private lateinit var onClick: (View, User) -> Unit
    private lateinit var onDelete: (User) -> Unit
    private lateinit var onItemSwipe: (User) -> Unit

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

    fun setOnDeleteUserListener(onClick: (User) -> Unit) {
        this.onDelete = onClick
    }

    fun setOnItemSwipeListener(onItemSwipe: (User) -> Unit) {
        this.onItemSwipe = onItemSwipe
    }

    override fun onSwiped(holder: RecyclerView.ViewHolder) {
        this.onItemSwipe((holder as UserViewHolder).user)
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