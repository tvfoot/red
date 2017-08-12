package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowLeagueBinding
import com.benoitquenaudon.tvfoot.red.injection.scope.FragmentScope
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@FragmentScope class FiltersAdapter @Inject constructor() : RecyclerView.Adapter<FiltersViewHolder>() {
  private var filters = emptyList<FilterRowDisplayable>()
  val filterRowClickObservable: PublishSubject<FilterRowDisplayable> =
      PublishSubject.create<FilterRowDisplayable>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)

    return when (viewType) {
      R.layout.filters_row_league -> FiltersViewHolder(binding as FiltersRowLeagueBinding, this)
      else -> throw UnsupportedOperationException(
          "don't know how to deal with this viewType: " + viewType)
    }
  }

  override fun getItemViewType(position: Int): Int =
      when (filters[position]) {
        is FilterRowDisplayable -> R.layout.filters_row_league
        else -> throw NotImplementedError("how about this position $position")
      }

  override fun onBindViewHolder(holder: FiltersViewHolder, position: Int) {
    holder.bind(filters[position])
  }

  override fun onViewRecycled(holder: FiltersViewHolder?) {
    holder?.unbind()
    super.onViewRecycled(holder)
  }

  override fun getItemCount(): Int = filters.size

  fun onClick(filter: FilterRowDisplayable) = filterRowClickObservable.onNext(filter)

  fun updateFilters(filters: List<FilterRowDisplayable>) {
    // TODO(benoit) diffUtil here...?
    this.filters = filters
    notifyDataSetChanged()
  }
}