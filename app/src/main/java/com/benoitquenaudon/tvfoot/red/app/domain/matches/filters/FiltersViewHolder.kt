package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowCompetitionBinding
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowTeamSearchBinding
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowTeamSearchResultBinding

sealed class FiltersViewHolder<out B : ViewDataBinding, in T : FiltersItemDisplayable>(
    val binding: B, val adapter: FiltersAdapter
) : RecyclerView.ViewHolder(binding.root) {
  abstract fun bind(item: T)
  abstract fun unbind()

  class FilterCompetitionViewHolder(
      binding: FiltersRowCompetitionBinding,
      adapter: FiltersAdapter
  ) : FiltersViewHolder<FiltersRowCompetitionBinding, FiltersCompetitionDisplayable>(binding,
      adapter) {

    override fun bind(item: FiltersCompetitionDisplayable) {
      binding.filter = item
      binding.handler = adapter
      binding.executePendingBindings()
    }

    override fun unbind() {
      binding.handler = null
    }
  }

  class FilterTeamSearchViewHolder(
      binding: FiltersRowTeamSearchBinding,
      adapter: FiltersAdapter
  ) : FiltersViewHolder<FiltersRowTeamSearchBinding, FiltersTeamSearchInputDisplayable>(binding,
      adapter) {
    override fun bind(item: FiltersTeamSearchInputDisplayable) {
      binding.filter = item
      binding.handler = adapter
      binding.executePendingBindings()
      // TODO(benoit) clean search result when losing focus
      // binding.filterInput.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
      //   Timber.d("CONNARD $hasFocus $v")
      // }
    }

    override fun unbind() {
      binding.handler = null
    }
  }

  class FilterTeamSearchResultViewHolder(
      binding: FiltersRowTeamSearchResultBinding,
      adapter: FiltersAdapter
  ) : FiltersViewHolder<FiltersRowTeamSearchResultBinding, FiltersTeamSearchResultDisplayable>(
      binding, adapter) {
    override fun bind(item: FiltersTeamSearchResultDisplayable) {
      binding.team = item
      binding.handler = adapter
      binding.executePendingBindings()
    }

    override fun unbind() {
      binding.handler = null
    }
  }
}