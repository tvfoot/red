package com.benoitquenaudon.tvfoot.red.app.domain.matches

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.HeaderRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.LoadingRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayable
import com.benoitquenaudon.tvfoot.red.databinding.MatchesRowHeaderBinding
import com.benoitquenaudon.tvfoot.red.databinding.MatchesRowMatchBinding
import com.benoitquenaudon.tvfoot.red.databinding.MatchesRowTeamlessMatchBinding
import com.benoitquenaudon.tvfoot.red.databinding.RowLoadingBinding


sealed class MatchesItemViewHolder<out B : ViewDataBinding, in T : MatchesItemDisplayable>(
    val binding: B
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

  abstract fun bind(item: T)
  abstract fun unbind()

  class MatchHeaderViewHolder(
      binding: MatchesRowHeaderBinding
  ) : MatchesItemViewHolder<MatchesRowHeaderBinding, HeaderRowDisplayable>(binding) {

    override fun bind(item: HeaderRowDisplayable) {
      binding.dayHeader = item
      binding.executePendingBindings()
    }

    override fun unbind() {
      binding.dayHeader = null
      binding.executePendingBindings()
    }
  }

  class MatchRowViewHolder(
      binding: MatchesRowMatchBinding,
      val handler: MatchesAdapter
  ) : MatchesItemViewHolder<MatchesRowMatchBinding, MatchRowDisplayable>(binding) {

    override fun bind(item: MatchRowDisplayable) {
      binding.match = item
      binding.handler = handler
      binding.executePendingBindings()

      setBroadcastsAdapter(item)
    }

    override fun unbind() {
      binding.match = null
      binding.handler = null
      binding.matchBroadcasters.adapter = null
      binding.executePendingBindings()
    }

    private fun setBroadcastsAdapter(match: MatchRowDisplayable) {
      val recyclerView = binding.matchBroadcasters

      val broadcastersAdapter = BroadcastersAdapter()
      broadcastersAdapter.addAll(match.broadcasters)

      recyclerView.run {
        adapter = broadcastersAdapter
        // To allow click events to go upward
        isLayoutFrozen = true
      }
    }
  }

  class MatchTeamlessRowViewHolder(
      binding: MatchesRowTeamlessMatchBinding,
      val handler: MatchesAdapter
  ) : MatchesItemViewHolder<MatchesRowTeamlessMatchBinding, MatchRowDisplayable>(binding) {

    override fun bind(item: MatchRowDisplayable) {
      binding.match = item
      binding.handler = handler
      binding.executePendingBindings()

      setBroadcastsAdapter(item)
    }

    override fun unbind() {
      binding.match = null
      binding.handler = null
      binding.matchBroadcasters.adapter = null
      binding.executePendingBindings()
    }

    private fun setBroadcastsAdapter(match: MatchRowDisplayable) {
      val recyclerView = binding.matchBroadcasters

      val broadcastersAdapter = BroadcastersAdapter()
      broadcastersAdapter.addAll(match.broadcasters)

      recyclerView.run {
        adapter = broadcastersAdapter
        // To allow click events to go upward
        isLayoutFrozen = true
      }
    }
  }

  class LoadingRowViewHolder(
      binding: RowLoadingBinding
  ) : MatchesItemViewHolder<RowLoadingBinding, LoadingRowDisplayable>(binding) {

    override fun bind(item: LoadingRowDisplayable) {
      // nothing to do
    }

    override fun unbind() {
      // nothing to do
    }
  }
}