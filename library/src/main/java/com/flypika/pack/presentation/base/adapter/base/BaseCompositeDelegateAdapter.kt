package com.invest.megapolis.presentation.base.adapter.base

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

open class BaseCompositeDelegateAdapter<T>(
    typeToAdapterMap: SparseArray<IDelegateAdapter<*, *>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val FIRST_VIEW_TYPE = 0
    }

    protected val data: MutableList<T> = ArrayList()
    private val typeToAdapterMap =
        typeToAdapterMap as SparseArray<IDelegateAdapter<RecyclerView.ViewHolder, T>>

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        for (i in FIRST_VIEW_TYPE until typeToAdapterMap.size()) {
            if (typeToAdapterMap.valueAt(i).isForViewType(data, position)) {
                return typeToAdapterMap.keyAt(i)
            }
        }
        throw NullPointerException("Can not get viewType for position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        typeToAdapterMap.get(viewType).onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val delegateAdapter = typeToAdapterMap.get(getItemViewType(position))
        if (delegateAdapter != null) {
            delegateAdapter.onBindViewHolder(holder, data, position)
        } else {
            throw NullPointerException("can not find adapter for position $position")
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        typeToAdapterMap.get(holder.itemViewType).onRecycled(holder)
    }

    open fun setItems(newData: List<T>) {
        data.run {
            clear()
            addAll(newData)
        }
        notifyDataSetChanged()
    }

    open fun getItems() = data.toList()

    class Builder<T> {

        private var count: Int = 0
        private val typeToAdapterMap: SparseArray<IDelegateAdapter<*, *>> = SparseArray()

        fun add(delegateAdapter: IDelegateAdapter<*, *>): Builder<T> {
            typeToAdapterMap.put(count++, delegateAdapter)
            return this
        }

        fun build(): BaseCompositeDelegateAdapter<T> {
            if (count == 0) throw IllegalArgumentException("Register at least one adapter")
            return BaseCompositeDelegateAdapter(typeToAdapterMap)
        }
    }
}