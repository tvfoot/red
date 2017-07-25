package com.benoitquenaudon.tvfoot.red.app.domain.libraries

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.databinding.LibraryRowBinding
import javax.inject.Inject

class LibrariesAdapter @Inject constructor(
    val libraries: List<Library>
) : RecyclerView.Adapter<LibraryViewHolder>() {

  override fun getItemCount(): Int = libraries.size

  override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
    holder.bind(libraries[position])
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding: LibraryRowBinding = DataBindingUtil.inflate(layoutInflater, R.layout.library_row,
        parent, false)
    return LibraryViewHolder(binding)
  }
}