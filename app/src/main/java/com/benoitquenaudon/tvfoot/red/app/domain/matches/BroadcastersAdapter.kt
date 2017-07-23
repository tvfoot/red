package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.BroadcasterRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.injection.scope.ActivityScope
import com.benoitquenaudon.tvfoot.red.databinding.BroadcasterRowBinding
import java.util.ArrayList
import javax.inject.Inject

@ActivityScope class BroadcastersAdapter @Inject constructor(
) : RecyclerView.Adapter<BroadcastersAdapter.BroadcasterViewHolder>() {
  private val broadcasters = ArrayList<BroadcasterRowDisplayable>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BroadcasterViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)

    val binding = DataBindingUtil.inflate<BroadcasterRowBinding>(layoutInflater,
        R.layout.broadcaster_row, parent, false)
    return BroadcasterViewHolder(binding)
  }

  override fun onBindViewHolder(holder: BroadcasterViewHolder, position: Int) {
    holder.bind(broadcasters[position])
  }

  override fun getItemCount(): Int = broadcasters.size

  fun add(broadcaster: BroadcasterRowDisplayable) = broadcasters.add(broadcaster)

  fun addAll(broadcasters: Collection<BroadcasterRowDisplayable>) {
    this.broadcasters.addAll(broadcasters)
  }

  class BroadcasterViewHolder(
      private val binding: BroadcasterRowBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(broadcaster: BroadcasterRowDisplayable) {
      binding.broadcasterLogoPath = broadcaster.logoPath()
      binding.executePendingBindings()
    }
  }
}
