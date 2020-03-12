package com.flypika.pack.presentation.base.recycler_view

import androidx.recyclerview.widget.RecyclerView

abstract class PagedAdapter<D, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    var pagingListWrapper: PagingListWrapper<D>? = null
        set(value) {
            notifyItemRangeRemoved(0, itemCount)

            field?.detachAdapter()

            field = value
            field?.attachAdapter(this)
        }

    override fun getItemCount(): Int = pagingListWrapper?.itemCount ?: 0

    protected fun getItem(pos: Int): D? = pagingListWrapper?.getItem(pos)
}
