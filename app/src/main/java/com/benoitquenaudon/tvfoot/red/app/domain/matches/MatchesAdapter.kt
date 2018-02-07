package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.util.DiffUtil.DiffResult
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesItemViewHolder.LoadingRowViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesItemViewHolder.MatchHeaderViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesItemViewHolder.MatchRowViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesItemViewHolder.MatchTeamlessRowViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.HeaderRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.LoadingRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayableDiffUtilCallback
import com.benoitquenaudon.tvfoot.red.databinding.MatchesRowHeaderBinding
import com.benoitquenaudon.tvfoot.red.databinding.MatchesRowMatchBinding
import com.benoitquenaudon.tvfoot.red.databinding.MatchesRowTeamlessMatchBinding
import com.benoitquenaudon.tvfoot.red.databinding.RowLoadingBinding
import com.benoitquenaudon.tvfoot.red.injection.scope.ActivityScope
import com.benoitquenaudon.tvfoot.red.util.DataVersion
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@ActivityScope
class MatchesAdapter @Inject constructor(
    private val schedulerProvider: BaseSchedulerProvider
) : RecyclerView.Adapter<MatchesItemViewHolder<*, *>>() {
  private var matchesItems = emptyList<MatchesItemDisplayable>()
  private val matchesObservable: PublishSubject<Triple<List<MatchesItemDisplayable>, List<MatchesItemDisplayable>, DataVersion>> =
      PublishSubject.create()
  val matchRowClickObservable: PublishSubject<MatchRowDisplayable> = PublishSubject.create()

  init {
    // calling it to enable subscription
    processItemDiffs()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesItemViewHolder<*, *> {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)

    return when (viewType) {
      R.layout.matches_row_header ->
        MatchHeaderViewHolder(binding as MatchesRowHeaderBinding)
      R.layout.matches_row_match ->
        MatchRowViewHolder(binding as MatchesRowMatchBinding, this)
      R.layout.matches_row_teamless_match ->
        MatchTeamlessRowViewHolder(binding as MatchesRowTeamlessMatchBinding, this)
      R.layout.row_loading ->
        LoadingRowViewHolder(binding as RowLoadingBinding)
      else -> throw UnsupportedOperationException(
          "don't know how to deal with this viewType: " + viewType)
    }
  }

  override fun onBindViewHolder(holder: MatchesItemViewHolder<*, *>, position: Int) {
    val item: MatchesItemDisplayable = matchesItems[position]
    return when (holder) {
      is MatchHeaderViewHolder -> {
        if (item is HeaderRowDisplayable) {
          holder.bind(item)
        } else {
          throw IllegalStateException("Wrong item for MatchHeaderViewHolder $item")
        }
      }
      is MatchRowViewHolder -> {
        if (item is MatchRowDisplayable) {
          holder.bind(item)
        } else {
          throw IllegalStateException("Wrong item for MatchRowViewHolder $item")
        }
      }
      is MatchTeamlessRowViewHolder -> {
        if (item is MatchRowDisplayable) {
          holder.bind(item)
        } else {
          throw IllegalStateException("Wrong item for MatchTeamlessRowViewHolder $item")
        }
      }
      is LoadingRowViewHolder -> {
        if (item == LoadingRowDisplayable) {
          holder.bind(LoadingRowDisplayable)
        } else {
          throw IllegalStateException("Wrong item for LoadingRowViewHolder $item")
        }
      }
    }
  }

  override fun onViewRecycled(holder: MatchesItemViewHolder<*, *>?) {
    holder?.unbind()
    super.onViewRecycled(holder)
  }

  override fun getItemCount(): Int {
    return matchesItems.size
  }

  override fun getItemViewType(position: Int): Int =
      matchesItems[position].let { item ->
        when (item) {
          is MatchRowDisplayable -> if (item.homeTeam.name.isNullOrEmpty()) {
            R.layout.matches_row_teamless_match
          } else {
            R.layout.matches_row_match
          }
          is HeaderRowDisplayable -> R.layout.matches_row_header
          LoadingRowDisplayable -> R.layout.row_loading
          else -> throw UnsupportedOperationException(
              "Don't know how to deal with this item: $matchesItems[position]")
        }
      }

  fun onClick(match: MatchRowDisplayable) {
    matchRowClickObservable.onNext(match)
  }

  // each time data is set, we update this variable so that if DiffUtil calculation returns
  // after repetitive updates, we can ignore the old calculation
  private var dataVersion = 0

  fun setMatchesItems(newItems: List<MatchesItemDisplayable>) {
    dataVersion++
    when {
      matchesItems.isEmpty() -> {
        if (newItems.isEmpty()) return

        matchesItems = newItems
        notifyDataSetChanged()
      }
      newItems.isEmpty() -> {
        val oldSize = matchesItems.size
        matchesItems = newItems
        notifyItemRangeRemoved(0, oldSize)
      }
      else -> matchesObservable.onNext(Triple(matchesItems, newItems, dataVersion))
    }
  }

  private fun processItemDiffs(): Disposable =
      matchesObservable
          .observeOn(schedulerProvider.computation())
          .scan(Triple(emptyList(), null, 0),
              { _: Triple<List<MatchesItemDisplayable>, DiffResult?, DataVersion>, (oldItems, newItems, startVersion) ->
                Triple(
                    newItems,
                    DiffUtil.calculateDiff(
                        MatchesItemDisplayableDiffUtilCallback(oldItems, newItems), true),
                    startVersion)
              })
          .skip(1)
          .observeOn(schedulerProvider.ui())
          .subscribe { (newItems, diffResult, startVersion) ->
            if (startVersion != dataVersion) {
              // ignore update
              return@subscribe
            }

            matchesItems = newItems
            diffResult?.dispatchUpdatesTo(this)
          }

  fun closestHeaderPosition(position: Int): Int {
    if (position < 0) throw IllegalStateException("should have a header")

    return matchesItems[position].let {
      if (it is HeaderRowDisplayable) position else closestHeaderPosition(position - 1)
    }
  }
}
