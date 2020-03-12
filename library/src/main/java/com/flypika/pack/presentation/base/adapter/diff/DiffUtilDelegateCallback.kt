package com.flypika.pack.presentation.base.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.flypika.pack.presentation.base.adapter.model.IAdapterComparableItem

class DiffUtilDelegateCallback(
    private val oldList: List<IAdapterComparableItem>,
    private val newList: List<IAdapterComparableItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id() == newList[newItemPosition].id()

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].content() == newList[newItemPosition].content()

}