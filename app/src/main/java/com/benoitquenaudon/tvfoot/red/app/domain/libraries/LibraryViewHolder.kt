package com.benoitquenaudon.tvfoot.red.app.domain.libraries

import android.support.v7.widget.RecyclerView
import androidx.net.toUri
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.databinding.LibraryRowBinding
import com.benoitquenaudon.tvfoot.red.util.CircleTransform
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

class LibraryViewHolder(
    val binding: LibraryRowBinding,
    val adapter: LibrariesAdapter
) : RecyclerView.ViewHolder(binding.root) {
  var library: Library by Delegates.notNull<Library>()

  fun bind(library: Library) {
    this.library = library
    binding.library = library
    binding.handler = adapter

    Picasso.get()
        .load(library.imageUrl.toUri())
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