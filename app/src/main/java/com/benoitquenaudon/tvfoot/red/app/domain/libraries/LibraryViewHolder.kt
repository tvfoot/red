package com.benoitquenaudon.tvfoot.red.app.domain.libraries

import android.support.v7.widget.RecyclerView
import com.benoitquenaudon.tvfoot.red.databinding.LibraryRowBinding

class LibraryViewHolder(
    val binding: LibraryRowBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(library: Library) {
    binding.library = library
    binding.executePendingBindings()
  }
}