package com.flypika.pack.presentation.base.recycler_view

import androidx.recyclerview.widget.DiffUtil
import com.flypika.pack.presentation.ext.getDiffUtilCallback
import com.flypika.pack.presentation.ext.safeResume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

typealias LoadListener<D> = (start: Int, count: Int, onLoaded: (List<D>) -> Unit) -> Unit

class PagingListWrapper<D>(
    coroutineScope: CoroutineScope,
    private val itemCallback: DiffUtil.ItemCallback<D>,
    private val pageSize: Int = DEFAULT_PAGE_SIZE,
    private val prefetchDistance: Int = DEFAULT_PREFETCH_DISTANCE,
    private val loadListener: LoadListener<D>
) {

    private val newDataChannel = BroadcastChannel<List<D>>(Channel.BUFFERED)

    private var pagedAdapter: PagedAdapter<D, *>? = null

    private var notRequestedPos = 0

    private var lastLoadedPos = 0

    var adapterData: List<D> = emptyList()
        private set

    var actualData: List<D> = emptyList()
        private set

    init {
        newDataChannel.asFlow()
            .scan(adapterData) { prev: List<D>, next: List<D> ->
                val pagedAdapter = this.pagedAdapter ?: return@scan next
                val result = withContext(Dispatchers.Default) { calculateDiff(prev, next) }
                withContext(Dispatchers.Main) {
                    adapterData = next
                    result.dispatchUpdatesTo(pagedAdapter)
                }
                next
            }.launchIn(coroutineScope)
    }

    fun adjustNotRequestedPos(adjustValue: Int) {
        notRequestedPos += adjustValue
        if (notRequestedPos < 0) notRequestedPos = 0
    }

    fun submitData(newData: List<D>) {
        actualData = newData
        newDataChannel.offer(newData)
    }

    fun addData(newData: List<D>, requestedPos: Int) {
        val mutable = actualData.toMutableList()
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
        return adapterData[pos]
    }

    internal val itemCount get() = adapterData.size

    internal fun attachAdapter(pagedAdapter: PagedAdapter<D, *>) {
        this.pagedAdapter = pagedAdapter
        requestLoad()
    }

    internal fun detachAdapter() {
        pagedAdapter = null
    }

    private fun checkNeedLoad(pos: Int) {
        if (notRequestedPos - pos <= prefetchDistance ||
            notRequestedPos < lastLoadedPos
        ) {
            requestLoad()
        }
    }

    private fun requestLoad() {
        val pos = notRequestedPos
        loadListener(pos, pageSize) {
            lastLoadedPos = pos + it.size
            addData(it, pos)
        }
        notRequestedPos += pageSize
    }

    private suspend fun calculateDiff(prev: List<D>, next: List<D>): DiffUtil.DiffResult =
        suspendCancellableCoroutine {
            val callback = itemCallback.getDiffUtilCallback(prev, next)
            val result = DiffUtil.calculateDiff(callback)
            it.safeResume(result)
        }

    companion object {

        private const val DEFAULT_PAGE_SIZE = 30
        private const val DEFAULT_PREFETCH_DISTANCE = 10
    }
}
