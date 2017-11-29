package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.util.DiffUtil.DiffResult
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersViewHolder.FilterCompetitionViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersViewHolder.FilterTeamSearchViewHolder
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowCompetitionBinding
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowTeamSearchBinding
import com.benoitquenaudon.tvfoot.red.injection.scope.FragmentScope
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@FragmentScope
class FiltersAdapter @Inject constructor(
    private val schedulerProvider: BaseSchedulerProvider
) : RecyclerView.Adapter<FiltersViewHolder<*, *>>() {
  private var filterItems = emptyList<FiltersItemDisplayable>()
  private val filtersObservable: PublishSubject<Pair<List<FiltersItemDisplayable>, List<FiltersItemDisplayable>>> =
      PublishSubject.create()
  val filterItemClickObservable: PublishSubject<FiltersItemDisplayable> =
      PublishSubject.create()
  val filterSearchInputObservable: PublishSubject<String> = PublishSubject.create()

  init {
    // calling it to enable subscription
    processItemDiffs()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersViewHolder<*, *> {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)

    return when (viewType) {
      R.layout.filters_row_competition ->
        FilterCompetitionViewHolder(binding as FiltersRowCompetitionBinding, this)
      R.layout.filters_row_team_search ->
        FilterTeamSearchViewHolder(binding as FiltersRowTeamSearchBinding, this)
      else -> throw UnsupportedOperationException(
          "don't know how to deal with this viewType: " + viewType)
    }
  }

  @Suppress("USELESS_IS_CHECK")
  override fun getItemViewType(position: Int): Int =
      when (filterItems[position]) {
        is FiltersCompetitionDisplayable -> R.layout.filters_row_competition
        is FiltersTeamSearchDisplayable -> R.layout.filters_row_team_search
        else -> throw NotImplementedError("how about this position $position")
      }

  override fun onBindViewHolder(holder: FiltersViewHolder<*, *>, position: Int) {
    val item = filterItems[position]
    return when (holder) {
      is FilterCompetitionViewHolder -> {
        if (item is FiltersCompetitionDisplayable) {
          holder.bind(item)
        } else {
          throw IllegalStateException("Wrong item for FilterCompetitionViewHolder $item")
        }
      }
      is FilterTeamSearchViewHolder -> {
        if (item is FiltersTeamSearchDisplayable) {
          holder.bind(item)
        } else {
          throw IllegalStateException("Wrong item for FilterTeamSearchViewHolder $item")
        }
      }
    }
  }

  override fun onViewRecycled(holder: FiltersViewHolder<*, *>?) {
    holder?.unbind()
    super.onViewRecycled(holder)
  }

  override fun getItemCount(): Int = filterItems.size

  fun onClick(filter: FiltersItemDisplayable) = filterItemClickObservable.onNext(filter)

  fun onSearchInputChanged(text: CharSequence, start: Int, before: Int, count: Int) {
    filterSearchInputObservable.onNext(text.toString())
  }

  fun setFiltersItems(newItems: List<FiltersItemDisplayable>) {
    when {
      filterItems.isEmpty() -> {
        if (newItems.isEmpty()) return

        filterItems = newItems
        notifyDataSetChanged()
      }
      newItems.isEmpty() -> {
        val oldSize = filterItems.size
        filterItems = newItems
        notifyItemRangeRemoved(0, oldSize)
      }
      else -> filtersObservable.onNext(Pair(filterItems, newItems))
    }
  }

  private fun processItemDiffs(): Disposable =
      filtersObservable
          .scan(Pair(emptyList(), null),
              { _: Pair<List<FiltersItemDisplayable>, DiffResult?>, (oldItems, newItems) ->
                val callback = FiltersItemDisplayableDiffUtilCallback().apply {
                  bindItems(oldItems, newItems)
                }
                Pair(newItems, DiffUtil.calculateDiff(callback, true))
              })
          .skip(1)
          .subscribeOn(schedulerProvider.computation())
          .observeOn(schedulerProvider.ui())
          .subscribe { (newItems, diffResult) ->
            filterItems = newItems
            diffResult?.dispatchUpdatesTo(this)
          }
}