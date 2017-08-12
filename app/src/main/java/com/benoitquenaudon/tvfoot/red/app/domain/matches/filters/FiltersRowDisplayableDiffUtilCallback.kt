package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.support.v7.util.DiffUtil
import kotlin.properties.Delegates

class FiltersRowDisplayableDiffUtilCallback : DiffUtil.Callback() {
  private var oldItems by Delegates.notNull<List<FilterRowDisplayable>>()
  private var newItems by Delegates.notNull<List<FilterRowDisplayable>>()

  override fun getOldListSize(): Int {
    return oldItems.size
  }

  override fun getNewListSize(): Int {
    return newItems.size
  }

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldItem = oldItems[oldItemPosition]
    val newItem = newItems[newItemPosition]

    return oldItem.code == newItem.code
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldItem = oldItems[oldItemPosition]
    val newItem = newItems[newItemPosition]

    return oldItem == newItem
  }

  fun bindItems(oldItems: List<FilterRowDisplayable>, newItems: List<FilterRowDisplayable>) {
    this.oldItems = oldItems
    this.newItems = newItems
  }
}
