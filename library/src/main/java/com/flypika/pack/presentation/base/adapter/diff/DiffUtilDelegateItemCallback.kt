package com.invest.megapolis.presentation.base.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.invest.megapolis.presentation.base.adapter.model.IAdapterComparableItem

class DiffUtilDelegateItemCallback : DiffUtil.ItemCallback<IAdapterComparableItem>() {

    override fun areItemsTheSame(
        oldItem: IAdapterComparableItem,
        newItem: IAdapterComparableItem
    ): Boolean =
        oldItem.id() == newItem.id()

    override fun areContentsTheSame(
        oldItem: IAdapterComparableItem,
        newItem: IAdapterComparableItem
    ): Boolean =
        oldItem.content() == newItem.content()

}