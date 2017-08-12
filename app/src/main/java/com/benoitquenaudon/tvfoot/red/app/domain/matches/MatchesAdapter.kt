package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesItemViewHolder.LoadingRowViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesItemViewHolder.MatchHeaderViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesItemViewHolder.MatchRowViewHolder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.HeaderRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.LoadingRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayableDiffUtilCallback
import com.benoitquenaudon.tvfoot.red.databinding.MatchesRowHeaderBinding
import com.benoitquenaudon.tvfoot.red.databinding.MatchesRowMatchBinding
import com.benoitquenaudon.tvfoot.red.databinding.RowLoadingBinding
import com.benoitquenaudon.tvfoot.red.injection.scope.ActivityScope
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@ActivityScope class MatchesAdapter @Inject constructor() : RecyclerView.Adapter<MatchesItemViewHolder<*, *>>() {
  private var matchesItems = emptyList<MatchesItemDisplayable>()
  val matchRowClickObservable: PublishSubject<MatchRowDisplayable> =
      PublishSubject.create<MatchRowDisplayable>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesItemViewHolder<*, *> {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)

    return when (viewType) {
      R.layout.matches_row_header -> MatchesItemViewHolder.MatchHeaderViewHolder(
          binding as MatchesRowHeaderBinding)
      R.layout.matches_row_match -> MatchesItemViewHolder.MatchRowViewHolder(
          binding as MatchesRowMatchBinding, this)
      R.layout.row_loading -> MatchesItemViewHolder.LoadingRowViewHolder(
          binding as RowLoadingBinding)
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
      is LoadingRowViewHolder -> {
        if (item is LoadingRowDisplayable) {
          holder.bind(item)
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
      when (matchesItems[position]) {
        is MatchRowDisplayable -> R.layout.matches_row_match
        is HeaderRowDisplayable -> R.layout.matches_row_header
        is LoadingRowDisplayable -> R.layout.row_loading
        else -> throw UnsupportedOperationException(
            "Don't know how to deal with this item: $matchesItems[position]")
      }

  fun onClick(match: MatchRowDisplayable) {
    matchRowClickObservable.onNext(match)
  }

  private val diffUtilCallback = MatchesItemDisplayableDiffUtilCallback()

  fun setMatchesItems(newItems: List<MatchesItemDisplayable>) {
    val oldItems = this.matchesItems
    this.matchesItems = newItems

    diffUtilCallback.bindItems(oldItems, newItems)
    // TODO(benoit) calculate diff on worker thread
    // https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/ui/common/DataBoundListAdapter.java#L77
    DiffUtil.calculateDiff(diffUtilCallback, true).dispatchUpdatesTo(this)
  }
}
