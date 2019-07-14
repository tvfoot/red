package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.benoitquenaudon.rxdatabinding.databinding.RxObservableBoolean
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.ClearFilters
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.FilterInitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.SearchInputIntent.SearchTeamIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.SearchedTeamSelectedIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.ToggleFilterIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.ToggleFilterIntent.ToggleFilterBroadcasterIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.ToggleFilterIntent.ToggleFilterCompetitionIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.ToggleFilterIntent.ToggleFilterTeamIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesViewModel
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesViewState
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FiltersAppliableItem.FiltersBroadcasterDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FiltersAppliableItem.FiltersCompetitionDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FiltersAppliableItem.FiltersTeamDisplayable
import com.benoitquenaudon.tvfoot.red.app.mvi.MviView
import com.benoitquenaudon.tvfoot.red.databinding.FragmentFiltersBinding
import com.benoitquenaudon.tvfoot.red.util.errorHandlingSubscribe
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class FiltersFragment : DaggerFragment(), MviView<MatchesIntent, MatchesViewState> {
  @Inject lateinit var disposables: CompositeDisposable
  @Inject lateinit var bindingModel: FiltersBindingModel
  @Inject lateinit var filtersAdapter: FiltersAdapter
  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
  private val viewModel: MatchesViewModel by lazy(NONE) {
    // we need to use MatchesActivity to get only one instance of the MatchesViewModel
    ViewModelProviders.of(activity!!, viewModelFactory)
        .get(MatchesViewModel::class.java)
  }
  private val toolbarClicks: Observable<MenuItem> by lazy {
    RxToolbar.itemClicks(binding.filtersToolbar)
        .share()
  }

  lateinit var binding: FragmentFiltersBinding

  companion object Factory {
    fun newInstance(): FiltersFragment = FiltersFragment()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filters, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    binding.filtersToolbar.inflateMenu(R.menu.fragment_filters)

    binding.filtersList.apply {
      addItemDecoration(
          androidx.recyclerview.widget.DividerItemDecoration(
              context, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
          )
      )
      adapter = filtersAdapter
    }

    bind()
  }

  override fun onDestroyView() {
    disposables.dispose()
    super.onDestroyView()
  }

  override fun intents(): Observable<MatchesIntent> {
    return Observable.merge(
        Observable.merge(
            initialIntent(),
            clearFilterIntent(),
            filterClickIntent()
        ),
        Observable.merge(
            searchedTeamSelectedIntent(),
            searchTeamIntent()
        )
    )
  }

  private fun initialIntent(): Observable<FilterInitialIntent> =
    Observable.just(FilterInitialIntent)

  private fun clearFilterIntent(): Observable<ClearFilters> =
    toolbarClicks
        .filter { it.itemId == R.id.action_clear }
        .map { ClearFilters }

  private fun filterClickIntent(): Observable<ToggleFilterIntent> =
    filtersAdapter.filterItemClickObservable.map { item ->
      when (item) {
        is FiltersCompetitionDisplayable -> ToggleFilterCompetitionIntent(item.code)
        is FiltersTeamDisplayable -> ToggleFilterTeamIntent(item.code)
        is FiltersBroadcasterDisplayable -> ToggleFilterBroadcasterIntent(item.code)
      }
    }

  private fun searchTeamIntent(): Observable<SearchTeamIntent> =
    filtersAdapter.filterSearchInputObservable
        .distinctUntilChanged()
        .map(::SearchTeamIntent)

  private fun searchedTeamSelectedIntent(): Observable<SearchedTeamSelectedIntent> {
    return filtersAdapter.searchedTeamClickObservable
        .map(::SearchedTeamSelectedIntent)
  }

  override fun render(state: MatchesViewState) {
    bindingModel.updateFromState(state)
  }

  private fun bind() {
    disposables.add(viewModel.states().errorHandlingSubscribe(this::render))
    viewModel.processIntents(intents())
    disposables.add(
        RxObservableBoolean.propertyChanges(bindingModel.hasFilters)
            .startWith(bindingModel.hasFilters.get()) // fix for rotation
            .errorHandlingSubscribe {
              binding.filtersToolbar.menu.findItem(R.id.action_clear).isVisible = it
            }
    )
  }
}
