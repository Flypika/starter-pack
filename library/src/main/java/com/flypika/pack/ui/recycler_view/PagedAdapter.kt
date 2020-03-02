package com.flypika.pack.ui.recycler_view

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class PagedAdapter<D, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    var pagingListWrapper: PagingListWrapper<D>? = null
        set(value) {
            val oldData = field?.data
            field?.detachAdapter()

            field = value
            field?.attachAdapter(this)

            if (oldData != null) {
                onNewData(oldData, emptyList())
            }
        }

    abstract fun onNewData(oldData: List<D>, newData: List<D>)

    override fun getItemCount(): Int = pagingListWrapper?.itemCount ?: 0

    protected fun getItem(pos: Int): D? = pagingListWrapper?.getItem(pos)
}
