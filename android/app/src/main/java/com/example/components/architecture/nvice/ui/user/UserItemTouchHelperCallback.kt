package com.example.components.architecture.nvice.ui.user

import android.graphics.Canvas
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.components.architecture.nvice.R
import kotlinx.android.synthetic.main.item_user.view.*

class UserItemTouchHelperCallback(private val listener: OnItemTouchListener) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(view: RecyclerView, holder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)
    }

    override fun onMove(view: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foregroundView = (viewHolder as UserPagedListAdapter.UserViewHolder).itemView.vForeground
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as UserPagedListAdapter.UserViewHolder).itemView.vForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive)

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX != 0f) {
                viewHolder.itemView.ivDeleteIconEnd.visibility = if (dX < 0) View.VISIBLE else View.GONE
                viewHolder.itemView.ivDeleteIconStart.visibility = if (dX < 0) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as UserPagedListAdapter.UserViewHolder).itemView.vForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = (viewHolder as UserPagedListAdapter.UserViewHolder).itemView.vForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
    }

    override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(holder)
        when (direction) {
            ItemTouchHelper.START -> listener.onSwipedLeft(holder)
            ItemTouchHelper.END -> listener.onSwipedRight(holder)
        }
    }

    interface OnItemTouchListener {
        fun onSwiped(holder: RecyclerView.ViewHolder)
        fun onSwipedLeft(holder: RecyclerView.ViewHolder)
        fun onSwipedRight(holder: RecyclerView.ViewHolder)
    }
}