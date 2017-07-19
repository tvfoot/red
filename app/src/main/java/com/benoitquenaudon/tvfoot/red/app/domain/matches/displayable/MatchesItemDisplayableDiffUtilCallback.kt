package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

import android.support.v7.util.DiffUtil
import kotlin.properties.Delegates

class MatchesItemDisplayableDiffUtilCallback : DiffUtil.Callback() {
  private var oldItems by Delegates.notNull<List<MatchesItemDisplayable>>()
  private var newItems by Delegates.notNull<List<MatchesItemDisplayable>>()

  override fun getOldListSize(): Int {
    return oldItems.size
  }

  override fun getNewListSize(): Int {
    return newItems.size
  }

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldItem = oldItems[oldItemPosition]
    val newItem = newItems[newItemPosition]

    return oldItem.isSameAs(newItem)
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldItem = oldItems[oldItemPosition]
    val newItem = newItems[newItemPosition]

    return oldItem == newItem
  }

  fun bindItems(oldItems: List<MatchesItemDisplayable>, newItems: List<MatchesItemDisplayable>) {
    this.oldItems = oldItems
    this.newItems = newItems
  }
}
