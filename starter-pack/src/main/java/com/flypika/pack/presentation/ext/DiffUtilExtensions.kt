package com.flypika.pack.presentation.ext

import androidx.recyclerview.widget.DiffUtil

fun <T> DiffUtil.ItemCallback<T>.getDiffUtilCallback(
    oldList: List<T>,
    newList: List<T>
): DiffUtil.Callback = object : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return areItemsTheSame(old, new)
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return areContentsTheSame(old, new)
    }
}
