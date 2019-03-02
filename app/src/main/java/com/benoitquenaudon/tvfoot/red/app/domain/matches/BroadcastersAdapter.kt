package com.benoitquenaudon.tvfoot.red.app.domain.matches

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.BroadcasterRowDisplayable
import com.benoitquenaudon.tvfoot.red.databinding.BroadcasterRowBinding
import com.benoitquenaudon.tvfoot.red.injection.scope.ActivityScope
import java.util.ArrayList
import javax.inject.Inject

@ActivityScope
class BroadcastersAdapter @Inject constructor(
) : androidx.recyclerview.widget.RecyclerView.Adapter<BroadcastersAdapter.BroadcasterViewHolder>() {
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

  // This should not be executed async because we are freezing the layout right afterwards
  fun addAll(broadcasters: Collection<BroadcasterRowDisplayable>) {
    this.broadcasters.addAll(broadcasters)
  }

  class BroadcasterViewHolder(
      private val binding: BroadcasterRowBinding
  ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

    fun bind(broadcaster: BroadcasterRowDisplayable) {
      binding.broadcasterLogoPath = broadcaster.logoPath
      binding.executePendingBindings()
    }
  }
}
