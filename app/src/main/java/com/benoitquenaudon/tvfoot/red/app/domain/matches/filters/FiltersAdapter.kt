package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersViewHolder.FilterCompetitionViewHolder
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowCompetitionBinding
import com.benoitquenaudon.tvfoot.red.injection.scope.FragmentScope
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@FragmentScope
class FiltersAdapter @Inject constructor() : RecyclerView.Adapter<FiltersViewHolder<*, *>>() {
  private var filters = emptyList<FiltersItemDisplayable>()
  val filterItemClickObservable: PublishSubject<FiltersItemDisplayable> =
      PublishSubject.create<FiltersItemDisplayable>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersViewHolder<*, *> {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)

    return when (viewType) {
      R.layout.filters_row_competition ->
        FilterCompetitionViewHolder(binding as FiltersRowCompetitionBinding, this)
      else -> throw UnsupportedOperationException(
          "don't know how to deal with this viewType: " + viewType)
    }
  }

  @Suppress("USELESS_IS_CHECK")
  override fun getItemViewType(position: Int): Int =
      when (filters[position]) {
        is FiltersCompetitionDisplayable -> R.layout.filters_row_competition
        else -> throw NotImplementedError("how about this position $position")
      }

  override fun onBindViewHolder(holder: FiltersViewHolder<*, *>, position: Int) {
    val item = filters[position]
    return when (holder) {
      is FilterCompetitionViewHolder -> {
        if (item is FiltersCompetitionDisplayable) {
          holder.bind(item)
        } else {
          throw IllegalStateException("Wrong item for FilterCompetitionViewHolder $item")
        }
      }
    }
  }

  override fun onViewRecycled(holder: FiltersViewHolder<*, *>?) {
    holder?.unbind()
    super.onViewRecycled(holder)
  }

  override fun getItemCount(): Int = filters.size

  fun onClick(filter: FiltersItemDisplayable) = filterItemClickObservable.onNext(filter)

  private val diffUtilCallback = FiltersItemDisplayableDiffUtilCallback()

  fun updateFilters(newItems: List<FiltersCompetitionDisplayable>) {
    val oldItems = this.filters
    this.filters = newItems

    diffUtilCallback.bindItems(oldItems, newItems)
    DiffUtil.calculateDiff(diffUtilCallback, true).dispatchUpdatesTo(this)
  }
}