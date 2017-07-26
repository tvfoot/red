package com.benoitquenaudon.tvfoot.red.app.domain.libraries

import android.net.Uri
import android.support.v7.widget.RecyclerView
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.RedApp
import com.benoitquenaudon.tvfoot.red.databinding.LibraryRowBinding
import com.benoitquenaudon.tvfoot.red.util.CircleTransform
import com.squareup.picasso.RequestCreator
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

    val picassoRequestCreator: RequestCreator = RedApp.getApp(binding.libraryImage.context)
        .appComponent
        .picasso()
        .load(Uri.parse(library.imageUrl))
        .placeholder(R.drawable.avatar_placeholder)
    if (library.circleCrop) {
      picassoRequestCreator.transform(CircleTransform)
    }
    picassoRequestCreator.into(binding.libraryImage)

    binding.executePendingBindings()
  }
}