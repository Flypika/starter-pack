package com.flypika.pack.ui.recycler_view

import androidx.recyclerview.widget.RecyclerView


abstract class PagedAdapter<D, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    var pagingListWrapper: PagingListWrapper<D>? = null
        set(value) {
            field = value
            field?.attachAdapter(this)
        }

    override fun getItemCount(): Int = pagingListWrapper?.itemCount ?: 0

    protected fun getItem(pos: Int): D? = pagingListWrapper?.getItem(pos)
}
