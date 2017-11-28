package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.support.v7.util.DiffUtil
import kotlin.properties.Delegates

class FiltersItemDisplayableDiffUtilCallback : DiffUtil.Callback() {
  private var oldItems by Delegates.notNull<List<FiltersItemDisplayable>>()
  private var newItems by Delegates.notNull<List<FiltersItemDisplayable>>()

  override fun getOldListSize(): Int {
    return oldItems.size
  }

  override fun getNewListSize(): Int {
    return newItems.size
  }

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldItems[oldItemPosition].isSameAs(newItems[newItemPosition])
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldItem = oldItems[oldItemPosition]
    val newItem = newItems[newItemPosition]

    return oldItem == newItem
  }

  fun bindItems(oldItems: List<FiltersItemDisplayable>, newItems: List<FiltersItemDisplayable>) {
    this.oldItems = oldItems
    this.newItems = newItems
  }
}
