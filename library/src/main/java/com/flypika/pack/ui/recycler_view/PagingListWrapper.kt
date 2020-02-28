package com.flypika.pack.ui.recycler_view

import androidx.recyclerview.widget.DiffUtil

typealias LoadListener<D> = (start: Int, count: Int, onLoaded: (List<D>) -> Unit) -> Unit

class PagingListWrapper<D>(
    private val pageSize: Int = DEFAULT_PAGE_SIZE,
    private val prefetchDistance: Int = DEFAULT_PREFETCH_DISTANCE,
    private val diffUtilCallback: DiffUtil.ItemCallback<D>,
    private val loadListener: LoadListener<D>
) {

    private lateinit var pagedAdapter: PagedAdapter<D, *>

    private var data: List<D> = emptyList()

    private var notRequestedPos = 0

    fun submitData(newData: List<D>) {
        val oldData = data
        data = newData

        if (this::pagedAdapter.isInitialized) {
            val callback = getCallback(oldData, newData)
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(pagedAdapter)
        }
    }

    fun addData(newData: List<D>, requestedPos: Int) {
        val mutable = data.toMutableList()
        for (i in newData.indices) {
            if (i + requestedPos in mutable.indices) {
                mutable[i + requestedPos] = newData[i]
            } else if (i + requestedPos == mutable.size) {
                mutable += newData[i]
            }
        }
        submitData(mutable)
    }

    internal fun getItem(pos: Int): D {
        checkNeedLoad(pos)
        return data[pos]
    }

    internal val itemCount get() = data.size

    internal fun attachAdapter(pagedAdapter: PagedAdapter<D, *>) {
        pagedAdapter.notifyDataSetChanged()
        this.pagedAdapter = pagedAdapter
        requestLoad()
    }

    private fun checkNeedLoad(pos: Int) {
        if (notRequestedPos - pos <= prefetchDistance &&
            notRequestedPos <= data.size
        ) {
            requestLoad()
        }
    }

    private fun requestLoad() {
        val pos = notRequestedPos
        loadListener(pos, pageSize) { addData(it, pos) }
        notRequestedPos += pageSize
    }

    private fun getCallback(oldList: List<D>, newList: List<D>): DiffUtil.Callback =
        object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val old = oldList[oldItemPosition]
                val new = newList[newItemPosition]
                return diffUtilCallback.areItemsTheSame(old, new)
            }

            override fun getOldListSize(): Int = oldList.size

            override fun getNewListSize(): Int = newList.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val old = oldList[oldItemPosition]
                val new = newList[newItemPosition]
                return diffUtilCallback.areContentsTheSame(old, new)
            }
        }

    companion object {

        private const val DEFAULT_PAGE_SIZE = 30
        private const val DEFAULT_PREFETCH_DISTANCE = 10
    }
}
