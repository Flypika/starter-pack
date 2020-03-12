package com.flypika.pack.presentation.base.adapter.regular

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {

    private var listener: ItemPopulateListener? = null

    fun setListener(listener: ItemPopulateListener) {
        this.listener = listener
    }

    fun bind(item: Any) {
        listener?.inflated(item, itemView)
    }

    interface ItemPopulateListener {
        fun inflated(viewType: Any, view: View)
    }
}