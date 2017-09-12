package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.benoitquenaudon.rxdatabinding.databinding.RxObservableBoolean
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.common.BaseActivity
import com.benoitquenaudon.tvfoot.red.app.common.flowcontroller.FlowController
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersFragment
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent.InitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent.LoadNextPageIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent.RefreshIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesStateBinder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesViewState
import com.benoitquenaudon.tvfoot.red.app.mvi.MviView
import com.benoitquenaudon.tvfoot.red.databinding.ActivityMatchesBinding
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.properties.Delegates


class MatchesActivity : BaseActivity(), MviView<MatchesIntent, MatchesViewState> {
  @Inject lateinit var adapter: MatchesAdapter
  @Inject lateinit var flowController: FlowController
  @Inject lateinit var viewBinding: MatchesViewBinding
  @Inject lateinit var stateBinder: MatchesStateBinder
  @Inject lateinit var disposables: CompositeDisposable

  private var binding: ActivityMatchesBinding by Delegates.notNull<ActivityMatchesBinding>()

  companion object {
    private const val FRAGMENT_FILTERS = "fragment:filters"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityComponent.inject(this)

    setupView()
    bind()
  }

  private fun setupView() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_matches)
    binding.recyclerView.adapter = adapter
    binding.recyclerView.addItemDecoration(MatchesHeaderDecoration(adapter), 0)
    binding.viewBinding = viewBinding

    setSupportActionBar(binding.matchesToolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)

    if (supportFragmentManager.findFragmentByTag(FRAGMENT_FILTERS) == null) {
      supportFragmentManager.beginTransaction()
          .add(R.id.filters, FiltersFragment.newInstance(), FRAGMENT_FILTERS)
          .commit()
    }
    val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
      setDrawable(ContextCompat.getDrawable(this@MatchesActivity, R.drawable.one_line_divider))
    }
    binding.recyclerView.addItemDecoration(divider)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.matches_settings_menu, menu)
    disposables.add(
        RxView.clicks(menu.findItem(R.id.matches_filters_item).actionView)
            .subscribe { openFiltersDrawer() }
    )

    return true
  }

  override fun onPrepareOptionsMenu(menu: Menu): Boolean {
    menu.findItem(R.id.matches_filters_item).isVisible = viewBinding.areTagsLoaded.get()

    menu.findItem(R.id.matches_filters_item).actionView
        .findViewById<View>(R.id.filters_usage_badge)
        .visibility = if (viewBinding.hasActiveFilters.get()) View.VISIBLE else View.GONE
    return super.onPrepareOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean =
      when (item.itemId) {
        R.id.matches_settings_item -> {
          flowController.toSettings()
          true
        }
        R.id.matches_filters_item -> {
          // should not be called because we use a actionLayout
          openFiltersDrawer()
          true
        }
        else -> super.onOptionsItemSelected(item)
      }

  @SuppressLint("RtlHardcoded")
  private fun openFiltersDrawer() {
    binding.drawerLayout.openDrawer(Gravity.RIGHT)
  }

  private fun bind() {
    disposables.add(stateBinder.states().subscribe(this::render))
    stateBinder.processIntents(intents())

    disposables.add(adapter.matchRowClickObservable
        .subscribe { matchRowDisplayable -> flowController.toMatch(matchRowDisplayable.matchId) }
    )
    disposables.add(
        RxObservableBoolean.propertyChanges(viewBinding.areTagsLoaded)
            .subscribe { invalidateOptionsMenu() }
    )
    disposables.add(
        RxObservableBoolean.propertyChanges(viewBinding.hasActiveFilters)
            .subscribe { invalidateOptionsMenu() }
    )

    disposables.add(
        RxView.clicks(binding.toolbarImageView).subscribe {
          (binding.recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition().let {
            if (it < 35) {
              binding.recyclerView.smoothScrollToPosition(0)
            } else {
              binding.recyclerView.scrollToPosition(0)
            }
          }
        }
    )
  }

  override fun onDestroy() {
    super.onDestroy()
    disposables.dispose()
  }

  override fun intents(): Observable<MatchesIntent> =
      Observable.merge(initialIntent(), refreshIntent(), loadNextPageIntent())

  private fun initialIntent(): Observable<InitialIntent> = Observable.just(InitialIntent)

  private fun refreshIntent(): Observable<RefreshIntent> {
    return RxSwipeRefreshLayout.refreshes(binding.swipeRefreshLayout).map { RefreshIntent }
  }

  private fun loadNextPageIntent(): Observable<LoadNextPageIntent> {
    return RxRecyclerView.scrollEvents(binding.recyclerView)
        .filter { viewBinding.hasMore && !viewBinding.nextPageLoading }
        .filter { scrollEvent ->
          val layoutManager = scrollEvent.view().layoutManager as LinearLayoutManager
          val lastPosition = layoutManager.findLastVisibleItemPosition()
          lastPosition == scrollEvent.view().adapter.itemCount - 1
        }
        .map { LoadNextPageIntent(viewBinding.currentPage + 1) }
  }

  override fun render(state: MatchesViewState) = viewBinding.updateFromState(state)

  override fun onBackPressed() {
    if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
      binding.drawerLayout.closeDrawer(GravityCompat.END)
    } else {
      super.onBackPressed()
    }
  }
}
