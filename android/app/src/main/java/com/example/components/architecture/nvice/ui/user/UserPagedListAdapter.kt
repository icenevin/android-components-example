package com.example.components.architecture.nvice.ui.user

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.model.UserStatus
import kotlinx.android.synthetic.main.item_user.view.*

class UserPagedListAdapter(private val context: Context) : PagedListAdapter<User, UserPagedListAdapter.UserViewHolder>(User.DIFF_CALLBACK), UserItemTouchHelperCallback.OnItemTouchListener {
    private lateinit var onClick: (User) -> Unit
    private lateinit var onDelete: (User) -> Unit
    private lateinit var onItemSwipe: (User) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder = UserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = holder.bind(getItem(position)!!, onClick, onDelete)

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var user: User
        fun bind(item: User, onClick: (User) -> Unit, onDelete: (User) -> Unit) = with(itemView) {
            user = item
            tvUserName.text = String.format("%s %s", item.firstName, item.lastName)
            tvUserStatusName.text = item.status?.name
            tvUserId.text = context.getString(R.string.item_user_id_text, item.getWorkerID())
            tvUserStatusName.generateTextColorByStatus(item.status)
            ivUserAvatar.generateAvatarBackgroundColorByStatus(item.status)
            btnDelete.setOnClickListener { onDelete(item) }
            setOnClickListener { onClick(item) }
        }
    }

    fun setOnClickListener(onClick: (User) -> Unit) {
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

fun AppCompatImageView.generateAvatarBackgroundColorByStatus(status: UserStatus?) {
    status?.let {
        when (it) {
            UserStatus.PERMANENT -> setBackgroundColor(ContextCompat.getColor(context, R.color.primary))
            UserStatus.TEMPORARY -> setBackgroundColor(ContextCompat.getColor(context, R.color.primary_dark))
            UserStatus.UNDEFINED -> setBackgroundColor(ContextCompat.getColor(context, R.color.black_500))
        }
    }
}

fun TextView.generateTextColorByStatus(status: UserStatus?) {
    status?.let {
        when (it) {
            UserStatus.PERMANENT -> setTextColor(ContextCompat.getColor(context, R.color.primary))
            UserStatus.TEMPORARY -> setTextColor(ContextCompat.getColor(context, R.color.primary_dark))
            UserStatus.UNDEFINED -> setTextColor(ContextCompat.getColor(context, R.color.black_500))
        }
    }
}