package com.example.components.architecture.nvice.ui.user

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import timber.log.Timber

class UserItemTouchHelperCallback(private val listener: OnItemTouchListener) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(view: RecyclerView, holder: RecyclerView.ViewHolder): Int {
        return ItemTouchHelper.Callback.makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)
    }

    override fun onMove(view: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(holder)
    }

    interface OnItemTouchListener {
        fun onSwiped(holder: RecyclerView.ViewHolder)
    }
}