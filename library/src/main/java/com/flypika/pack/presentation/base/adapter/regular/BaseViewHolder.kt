package com.invest.megapolis.presentation.base.adapter.regular

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {

    private var listener: ItemInflateListener? = null

    fun setListener(listener: ItemInflateListener) {
        this.listener = listener
    }

    fun bind(item: Any) {
        listener?.inflated(item, itemView)
    }

    interface ItemInflateListener {
        fun inflated(viewType: Any, view: View)
    }
}