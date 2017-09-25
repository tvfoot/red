package com.benoitquenaudon.tvfoot.red.app.domain.match

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.benoitquenaudon.tvfoot.red.R.layout
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.BroadcasterRowDisplayable
import com.benoitquenaudon.tvfoot.red.databinding.BroadcasterRowLargeBinding
import com.benoitquenaudon.tvfoot.red.injection.scope.ActivityScope
import java.util.ArrayList
import javax.inject.Inject

@ActivityScope
class MatchBroadcastersAdapter @Inject constructor(
) : RecyclerView.Adapter<MatchBroadcastersAdapter.MatchBroadcasterViewHolder>() {
  private val broadcasters = ArrayList<BroadcasterRowDisplayable>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchBroadcasterViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)

    val binding = DataBindingUtil.inflate<BroadcasterRowLargeBinding>(layoutInflater,
        layout.broadcaster_row_large, parent, false)
    return MatchBroadcasterViewHolder(binding)
  }

  override fun onBindViewHolder(holder: MatchBroadcasterViewHolder, position: Int) {
    holder.bind(broadcasters[position])
  }

  override fun getItemCount(): Int = broadcasters.size

  fun add(broadcaster: BroadcasterRowDisplayable) = broadcasters.add(broadcaster)

  fun addAll(broadcasters: Collection<BroadcasterRowDisplayable>) {
    this.broadcasters.addAll(broadcasters)
  }

  class MatchBroadcasterViewHolder(
      private val binding: BroadcasterRowLargeBinding
  ) : ViewHolder(binding.root) {

    fun bind(broadcaster: BroadcasterRowDisplayable) {
      binding.broadcasterLogoPath = broadcaster.logoPath()
      binding.executePendingBindings()
    }
  }
}