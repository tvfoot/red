package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.support.v7.widget.RecyclerView
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowLeagueBinding

class FiltersViewHolder(
    val binding: FiltersRowLeagueBinding, val adapter: FiltersAdapter
) : RecyclerView.ViewHolder(binding.root) {

  fun bind(filterRowDisplayable: FilterRowDisplayable) {
    binding.filter = filterRowDisplayable
    binding.handler = adapter
    binding.executePendingBindings()
  }

  fun unbind() {
    binding.handler = null
  }
}