package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.common.BaseActivity
import com.benoitquenaudon.tvfoot.red.app.common.flowcontroller.FlowController
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesStateBinder
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesViewState
import com.benoitquenaudon.tvfoot.red.databinding.ActivityMatchesBinding
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.properties.Delegates

class MatchesActivity : BaseActivity() {
  @Inject lateinit var adapter: MatchesAdapter
  @Inject lateinit var flowController: FlowController
  @Inject lateinit var viewModel: MatchesViewModel
  @Inject lateinit var stateBinder: MatchesStateBinder
  @Inject lateinit var disposables: CompositeDisposable

  private var binding: ActivityMatchesBinding by Delegates.notNull<ActivityMatchesBinding>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityComponent.inject(this)

    setupView()
    bind()
  }

  private fun setupView() {
    binding = DataBindingUtil.setContentView<ActivityMatchesBinding>(this,
        R.layout.activity_matches)
    binding.recyclerView.adapter = adapter
    binding.viewModel = viewModel

    setSupportActionBar(binding.matchesToolbar)
    actionBar?.setDisplayShowTitleEnabled(false)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.matches_settings_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.matches_settings_item) {
      flowController.toSettings()
      return true
    }

    return super.onOptionsItemSelected(item)
  }

  private fun bind() {
    disposables.add(stateBinder.statesAsObservable.subscribe(this::render))
    stateBinder.forwardIntents(intents())

    disposables.add(adapter.matchRowClickObservable
        .subscribe { matchRowDisplayable -> flowController.toMatch(matchRowDisplayable.matchId) })
  }

  override fun onDestroy() {
    super.onDestroy()
    disposables.dispose()
  }

  fun intents(): Observable<MatchesIntent> {
    return Observable.merge(initialIntent(), refreshIntent(), loadNextPageIntent())
  }

  private fun initialIntent(): Observable<MatchesIntent.InitialIntent> {
    return Observable.just(MatchesIntent.InitialIntent.create())
  }

  private fun refreshIntent(): Observable<MatchesIntent.RefreshIntent> {
    return RxSwipeRefreshLayout.refreshes(binding.swipeRefreshLayout)
        .map { MatchesIntent.RefreshIntent.create() }
  }

  private fun loadNextPageIntent(): Observable<MatchesIntent.LoadNextPageIntent> {
    return RxRecyclerView.scrollEvents(binding.recyclerView)
        .filter { viewModel.hasMore && !viewModel.nextPageLoading }
        .filter { scrollEvent ->
          val layoutManager = scrollEvent.view().layoutManager as LinearLayoutManager
          val lastPosition = layoutManager.findLastVisibleItemPosition()
          lastPosition == scrollEvent.view().adapter.itemCount - 1
        }
        .map { MatchesIntent.LoadNextPageIntent.create(viewModel.currentPage + 1) }
  }

  fun render(state: MatchesViewState) = viewModel.updateFromState(state)
}
