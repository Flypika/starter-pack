package com.flypika.pack.presentation.base.adapter.diff

import android.util.SparseArray
import androidx.recyclerview.widget.DiffUtil
import com.flypika.pack.presentation.base.adapter.base.BaseCompositeDelegateAdapter
import com.flypika.pack.presentation.base.adapter.base.IDelegateAdapter
import com.flypika.pack.presentation.base.adapter.model.IAdapterComparableItem

open class DiffUtilCompositeAdapter(
    typeToAdapterMap: SparseArray<IDelegateAdapter<*, *>>
) : BaseCompositeDelegateAdapter<IAdapterComparableItem>(typeToAdapterMap) {

    override fun setItems(newData: List<IAdapterComparableItem>) {
        DiffUtilDelegateCallback(data, newData).run {
            DiffUtil.calculateDiff(this).apply {
                data.run {
                    clear()
                    addAll(newData)
                }
                dispatchUpdatesTo(this@DiffUtilCompositeAdapter)
            }
        }
    }

    class Builder {

        private var count: Int = 0
        private val typeToAdapterMap: SparseArray<IDelegateAdapter<*, *>> = SparseArray()

        fun add(delegateAdapter: IDelegateAdapter<*, out IAdapterComparableItem>): Builder {
            typeToAdapterMap.put(count++, delegateAdapter)
            return this
        }

        fun build(): DiffUtilCompositeAdapter {
            if (count == 0) throw IllegalArgumentException("Register at least one adapter")
            return DiffUtilCompositeAdapter(typeToAdapterMap)
        }
    }
}