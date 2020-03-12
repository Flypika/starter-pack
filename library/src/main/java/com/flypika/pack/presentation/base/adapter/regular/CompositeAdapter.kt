package com.flypika.pack.presentation.base.adapter.regular

import android.util.SparseArray
import com.flypika.pack.presentation.base.adapter.base.BaseCompositeDelegateAdapter
import com.flypika.pack.presentation.base.adapter.base.IDelegateAdapter
import com.flypika.pack.presentation.base.adapter.model.IAdapterItem

open class CompositeAdapter(
    typeToAdapterMap: SparseArray<IDelegateAdapter<*, *>>
) : BaseCompositeDelegateAdapter<IAdapterItem>(typeToAdapterMap) {

    class Builder {

        private var count: Int = 0
        private val typeToAdapterMap: SparseArray<IDelegateAdapter<*, *>> = SparseArray()

        fun add(delegateAdapter: IDelegateAdapter<*, out IAdapterItem>): Builder {
            typeToAdapterMap.put(count++, delegateAdapter)
            return this
        }

        fun build(): CompositeAdapter {
            require(count != 0) { "Register at least one adapter" }
            return CompositeAdapter(typeToAdapterMap)
        }
    }
}