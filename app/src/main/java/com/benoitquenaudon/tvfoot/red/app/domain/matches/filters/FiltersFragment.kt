package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters


import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.common.BaseFragment
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent.ClearFilters
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent.ToggleFilterIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesStateBinder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesViewState
import com.benoitquenaudon.tvfoot.red.app.mvi.MviView
import com.benoitquenaudon.tvfoot.red.databinding.FragmentFiltersBinding
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FiltersFragment : BaseFragment(), MviView<MatchesIntent, MatchesViewState> {
  @Inject lateinit var disposables: CompositeDisposable
  @Inject lateinit var viewModel: FiltersViewModel
  @Inject lateinit var stateBinder: MatchesStateBinder
  @Inject lateinit var filtersAdapter: FiltersAdapter

  lateinit var binding: FragmentFiltersBinding

  companion object Factory {
    fun newInstance(): FiltersFragment = FiltersFragment()
  }

  @Suppress("OverridingDeprecatedMember", "DEPRECATION")
  override fun onAttach(activity: Activity?) {
    fragmentComponent.inject(this)
    super.onAttach(activity)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate<FragmentFiltersBinding>(inflater, R.layout.fragment_filters,
        container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.filtersToolbar.inflateMenu(R.menu.fragment_filters)

    binding.filtersList.apply {
      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
      adapter = filtersAdapter
    }

    bind()
  }

  override fun onDestroyView() {
    disposables.dispose()
    super.onDestroyView()
  }

  override fun intents(): Observable<MatchesIntent> {
    return Observable.merge(clearFilterIntent(), filterClickIntent())
  }

  private fun clearFilterIntent(): Observable<ClearFilters> =
      RxToolbar.itemClicks(binding.filtersToolbar)
          .filter { it.itemId == R.id.action_clear }.map { ClearFilters }

  private fun filterClickIntent(): Observable<ToggleFilterIntent> =
      filtersAdapter.filterRowClickObservable.map { ToggleFilterIntent(it.code) }

  override fun render(state: MatchesViewState) {
    viewModel.updateFromState(state)

    binding.filtersToolbar.menu.findItem(R.id.action_clear).isVisible = viewModel.hasFilters
  }

  private fun bind() {
    disposables.add(stateBinder.states().subscribe(this::render))
    stateBinder.processIntents(intents())
  }
}

//  private inner class FiltersHolder(view: View) : RecyclerView.ViewHolder(view) {
//    val title: TextView = view.findViewById(R.id.title)
////    val checkBox: CheckBox = view.findViewById(R.id.check_box)
//
//    init {
//      view.setOnClickListener(onItemClickListener)
//    }
//  }
//
//  private abstract inner class FiltersAdapter : RecyclerView.Adapter<FiltersHolder>() {
////
////    val filters = listOf(
////        BookmarkFilter,
////        TrackFilter.get(Session.Track.Cloud),
////        TrackFilter.get(Session.Track.Discovery),
////        TrackFilter.get(Session.Track.Mobile),
////        TrackFilter.get(Session.Track.Web))
////
////    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersHolder =
////        FiltersHolder(parent.inflate(viewType, false))
////
////    override fun getItemViewType(position: Int): Int = when (filters[position]) {
////      is BookmarkFilter -> R.layout.list_item_filter_bookmarks
////      else -> R.layout.list_item_filter_track
////    }
////
////    override fun getItemCount(): Int = filters.size
////
////    override fun onBindViewHolder(holder: FiltersHolder, position: Int) {
////      val filter = filters[position]
////      if (filter is TrackFilter) {
////        val track = filter.track
////        with(holder.title) {
////          text = track.getName(context)
////          setTextColor(track.foregroundColor)
////          setBackgroundColor(track.backgroundColor)
////        }
////      }
////      holder.checkBox.isChecked = filtersModel.isFilter(filters[position])
////    }
//  }
//
//  val onItemClickListener = View.OnClickListener { view ->
//    recyclerView!!.apply {
//      val position = getChildLayoutPosition(view)
//      if (position != RecyclerView.NO_POSITION) {
//        filtersAdapter?.let {
//          //          filtersModel.toggleFilter(it.filters[position])
//        }
////        (findViewHolderForLayoutPosition(position) as FiltersHolder).checkBox.toggle()
//        updateClearMenuItem()
//      }
//    }
//  }
