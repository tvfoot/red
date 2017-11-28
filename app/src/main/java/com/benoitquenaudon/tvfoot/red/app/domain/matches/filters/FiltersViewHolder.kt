package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowCompetitionBinding

sealed class FiltersViewHolder<out B : ViewDataBinding, in T : FiltersItemDisplayable>(
    val binding: B, val adapter: FiltersAdapter
) : RecyclerView.ViewHolder(binding.root) {
  abstract fun bind(item: T)
  abstract fun unbind()

  class FilterCompetitionViewHolder(
      binding: FiltersRowCompetitionBinding, adapter: FiltersAdapter
  ) : FiltersViewHolder<FiltersRowCompetitionBinding, FiltersCompetitionDisplayable>(binding, adapter) {

    override fun bind(item: FiltersCompetitionDisplayable) {
      binding.filter = item
      binding.handler = adapter
      binding.executePendingBindings()
    }

    override fun unbind() {
      binding.handler = null
    }
  }
}