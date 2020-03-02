package com.flypika.pack.ui.recycler_view

typealias LoadListener<D> = (start: Int, count: Int, onLoaded: (List<D>) -> Unit) -> Unit

class PagingListWrapper<D>(
    private val pageSize: Int = DEFAULT_PAGE_SIZE,
    private val prefetchDistance: Int = DEFAULT_PREFETCH_DISTANCE,
    private val loadListener: LoadListener<D>
) {

    private var pagedAdapter: PagedAdapter<D, *>? = null

    var data: List<D> = emptyList()
        private set

    private var notRequestedPos = 0

    fun adjustNotRequestedPos(adjustValue: Int) {
        notRequestedPos += adjustValue
        if (notRequestedPos < 0) notRequestedPos = 0
    }

    fun submitData(newData: List<D>) {
        val oldData = data
        data = newData

        pagedAdapter?.onNewData(oldData, newData)
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
        this.pagedAdapter = pagedAdapter
        requestLoad()
    }

    internal fun detachAdapter() {
        pagedAdapter = null
    }

    private fun checkNeedLoad(pos: Int) {
        if (notRequestedPos - pos <= prefetchDistance) {
            requestLoad()
        }
    }

    private fun requestLoad() {
        val pos = notRequestedPos
        loadListener(pos, pageSize) { addData(it, pos) }
        notRequestedPos += pageSize
    }

    companion object {

        private const val DEFAULT_PAGE_SIZE = 30
        private const val DEFAULT_PREFETCH_DISTANCE = 10
    }
}
