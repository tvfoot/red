package com.benoitquenaudon.tvfoot.red.app.domain.libraries

import android.net.Uri
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.databinding.LibraryRowBinding
import com.benoitquenaudon.tvfoot.red.util.CircleTransform
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

class LibraryViewHolder(
    val binding: LibraryRowBinding,
    val adapter: LibrariesAdapter
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
  var library: Library by Delegates.notNull()

  fun bind(library: Library) {
    this.library = library
    binding.library = library
    binding.handler = adapter

    Picasso.with(binding.root.context)
        .load(Uri.parse(library.imageUrl))
        .placeholder(R.drawable.avatar_placeholder)
        .apply {
          if (library.circleCrop) {
            transform(CircleTransform)
          }
        }
        .into(binding.libraryImage)

    binding.executePendingBindings()
  }
}