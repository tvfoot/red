package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

import androidx.recyclerview.widget.DiffUtil

class MatchesItemDisplayableDiffUtilCallback<out T : MatchesItemDisplayable>(
    private val oldItems: List<T>,
    private val newItems: List<T>
) : DiffUtil.Callback() {

  override fun getOldListSize(): Int {
    return oldItems.size
  }

  override fun getNewListSize(): Int {
    return newItems.size
  }

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      oldItems[oldItemPosition].isSameAs(newItems[newItemPosition])

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      oldItems[oldItemPosition] == newItems[newItemPosition]
}
