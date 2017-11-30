package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FilterSearchLoadingRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FiltersAppliableItem.FiltersCompetitionDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FiltersAppliableItem.FiltersTeamDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchInputDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchResultDisplayable
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowCompetitionBinding
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowTeamBinding
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowTeamSearchBinding
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowTeamSearchResultBinding
import com.benoitquenaudon.tvfoot.red.databinding.RowLoadingBinding

sealed class FiltersViewHolder<out B : ViewDataBinding, in T : FiltersItemDisplayable>(
    val binding: B
) : RecyclerView.ViewHolder(binding.root) {
  abstract fun bind(item: T)
  abstract fun unbind()

  class FilterCompetitionViewHolder(
      binding: FiltersRowCompetitionBinding,
      val adapter: FiltersAdapter
  ) : FiltersViewHolder<FiltersRowCompetitionBinding, FiltersCompetitionDisplayable>(binding) {

    override fun bind(item: FiltersCompetitionDisplayable) {
      binding.filter = item
      binding.handler = adapter
      binding.executePendingBindings()
    }

    override fun unbind() {
      binding.handler = null
    }
  }

  class FilterTeamViewHolder(
      binding: FiltersRowTeamBinding,
      val adapter: FiltersAdapter
  ) : FiltersViewHolder<FiltersRowTeamBinding, FiltersTeamDisplayable>(binding) {

    override fun bind(item: FiltersTeamDisplayable) {
      binding.team = item
      binding.handler = adapter
      binding.executePendingBindings()
    }

    override fun unbind() {
      binding.handler = null
    }
  }

  class FilterTeamSearchViewHolder(
      binding: FiltersRowTeamSearchBinding,
      val adapter: FiltersAdapter
  ) : FiltersViewHolder<FiltersRowTeamSearchBinding, TeamSearchInputDisplayable>(binding) {
    override fun bind(item: TeamSearchInputDisplayable) {
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
      val adapter: FiltersAdapter
  ) : FiltersViewHolder<FiltersRowTeamSearchResultBinding, TeamSearchResultDisplayable>(
      binding) {
    override fun bind(item: TeamSearchResultDisplayable) {
      binding.team = item
      binding.handler = adapter
      binding.executePendingBindings()
    }

    override fun unbind() {
      binding.handler = null
    }
  }

  class FilterSearchLoadingRowViewHolder(
      binding: RowLoadingBinding
  ) : FiltersViewHolder<RowLoadingBinding, FilterSearchLoadingRowDisplayable>(binding) {

    override fun bind(item: FilterSearchLoadingRowDisplayable) {
      // nothing to do
    }

    override fun unbind() {
      // nothing to do
    }
  }
}