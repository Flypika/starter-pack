package com.invest.megapolis.presentation.base.adapter.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface IDelegateAdapter<VH : RecyclerView.ViewHolder, T> {

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: VH, items: List<T>, position: Int)

    fun onRecycled(holder: VH)

    fun isForViewType(items: List<*>, position: Int): Boolean

}