package com.invest.megapolis.presentation.base.adapter.regular

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.invest.megapolis.presentation.base.adapter.base.IDelegateAdapter


abstract class BaseDelegateAdapter<VH : BaseViewHolder, T> : IDelegateAdapter<VH, T> {

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected abstract fun onBindViewHolder(view: View, item: T, viewHolder: VH)

    /**
     * @param parent inflated view
     */

    protected abstract fun createViewHolder(parent: View): VH

    override fun onRecycled(holder: VH) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)

        return createViewHolder(inflatedView).apply {
            setListener(object : BaseViewHolder.ItemInflateListener {
                override fun inflated(viewType: Any, view: View) {
                    onBindViewHolder(view, viewType as T, this@apply)
                }
            })
        }
    }

    override fun onBindViewHolder(holder: VH, items: List<T>, position: Int) {
        (holder as BaseViewHolder).bind(items[position] as Any)
    }
}