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
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FilterHeaderDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FilterSearchLoadingRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FiltersAppliableItem
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FiltersAppliableItem.FiltersCompetitionDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FiltersAppliableItem.FiltersTeamDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchInputDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchResultDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersViewHolder.FilterCompetitionViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersViewHolder.FilterEmptyViewHolder.FilterSearchLoadingRowViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersViewHolder.FilterHeaderViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersViewHolder.FilterTeamSearchResultViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersViewHolder.FilterTeamSearchViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersViewHolder.FilterTeamViewHolder
import com.benoitquenaudon.tvfoot.red.databinding.FiltersHeaderBinding
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowCompetitionBinding
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowTeamBinding
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowTeamSearchBinding
import com.benoitquenaudon.tvfoot.red.databinding.FiltersRowTeamSearchResultBinding
import com.benoitquenaudon.tvfoot.red.databinding.RowLoadingBinding
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
  val filterItemClickObservable: PublishSubject<FiltersAppliableItem> = PublishSubject.create()
  val filterSearchInputObservable: PublishSubject<String> = PublishSubject.create()
  val searchedTeamClickObservable: PublishSubject<TeamSearchResultDisplayable> =
      PublishSubject.create()

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
      R.layout.filters_row_team ->
        FilterTeamViewHolder(binding as FiltersRowTeamBinding, this)
      R.layout.filters_row_team_search ->
        FilterTeamSearchViewHolder(binding as FiltersRowTeamSearchBinding, this)
      R.layout.filters_row_team_search_result ->
        FilterTeamSearchResultViewHolder(binding as FiltersRowTeamSearchResultBinding, this)
      R.layout.row_loading ->
        FilterSearchLoadingRowViewHolder(binding as RowLoadingBinding)
      R.layout.filters_header ->
        FilterHeaderViewHolder(binding as FiltersHeaderBinding)
      else -> throw UnsupportedOperationException(
          "don't know how to deal with this viewType: " + viewType)
    }
  }

  override fun getItemViewType(position: Int): Int =
      when (filterItems[position]) {
        is FiltersCompetitionDisplayable -> R.layout.filters_row_competition
        is FiltersTeamDisplayable -> R.layout.filters_row_team
        is TeamSearchInputDisplayable -> R.layout.filters_row_team_search
        is TeamSearchResultDisplayable -> R.layout.filters_row_team_search_result
        is FilterSearchLoadingRowDisplayable -> R.layout.row_loading
        is FilterHeaderDisplayable -> R.layout.filters_header
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
        if (item is TeamSearchInputDisplayable) {
          holder.bind(item)
        } else {
          throw IllegalStateException("Wrong item for FilterTeamSearchViewHolder $item")
        }
      }
      is FilterTeamSearchResultViewHolder -> {
        if (item is TeamSearchResultDisplayable) {
          holder.bind(item)
        } else {
          throw IllegalStateException("Wrong item for FilterTeamSearchResultViewHolder $item")
        }
      }
      is FilterSearchLoadingRowViewHolder -> {
        // stateless view, no need to bind anything
      }
      is FilterHeaderViewHolder -> {
        if (item is FilterHeaderDisplayable) {
          holder.bind(item)
        } else {
          throw IllegalStateException("Wrong item for FilterHeaderViewHolder $item")
        }
      }
      is FilterTeamViewHolder ->
        if (item is FiltersTeamDisplayable) {
          holder.bind(item)
        } else {
          throw IllegalStateException("Wrong item for FilterTeamViewHolder $item")
        }
    }
  }

  override fun onViewRecycled(holder: FiltersViewHolder<*, *>?) {
    holder?.unbind()
    super.onViewRecycled(holder)
  }

  override fun getItemCount(): Int = filterItems.size

  fun onClick(filter: FiltersAppliableItem) = filterItemClickObservable.onNext(filter)

  @Suppress("UNUSED_PARAMETER")
  fun onSearchInputChanged(text: CharSequence, start: Int, before: Int, count: Int) {
    filterSearchInputObservable.onNext(text.toString())
  }

  fun onSearchTeamSelected(team: TeamSearchResultDisplayable) {
    searchedTeamClickObservable.onNext(team)
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