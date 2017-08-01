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
import com.benoitquenaudon.tvfoot.red.app.injection.scope.ActivityScope
import com.benoitquenaudon.tvfoot.red.databinding.MatchesRowHeaderBinding
import com.benoitquenaudon.tvfoot.red.databinding.MatchesRowMatchBinding
import com.benoitquenaudon.tvfoot.red.databinding.RowLoadingBinding
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@ActivityScope class MatchesAdapter @Inject constructor() : RecyclerView.Adapter<MatchesItemViewHolder<*, *>>() {
  private var matchesItems = emptyList<MatchesItemDisplayable>()
  val matchRowClickObservable = PublishSubject.create<MatchRowDisplayable>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesItemViewHolder<*, *> {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)

    when (viewType) {
      R.layout.matches_row_header -> return MatchesItemViewHolder.MatchHeaderViewHolder(
          binding as MatchesRowHeaderBinding)
      R.layout.matches_row_match -> return MatchesItemViewHolder.MatchRowViewHolder(
          binding as MatchesRowMatchBinding, this)
      R.layout.row_loading -> return MatchesItemViewHolder.LoadingRowViewHolder(
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
    super.onViewRecycled(holder)
    holder!!.unbind()
  }

  override fun getItemCount(): Int {
    return matchesItems.size
  }

  override fun getItemViewType(position: Int): Int {
    val item = matchesItems[position]
    if (item is MatchRowDisplayable) {
      return R.layout.matches_row_match
    }
    if (item is HeaderRowDisplayable) {
      return R.layout.matches_row_header
    }
    if (item is LoadingRowDisplayable) {
      return R.layout.row_loading
    }
    throw UnsupportedOperationException("Don't know how to deal with this item: " + item)
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
