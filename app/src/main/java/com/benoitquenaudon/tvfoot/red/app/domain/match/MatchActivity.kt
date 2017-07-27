package com.benoitquenaudon.tvfoot.red.app.domain.match

import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.benoitquenaudon.rxdatabinding.databinding.RxObservableBoolean
import com.benoitquenaudon.tvfoot.red.AUTHORITIES
import com.benoitquenaudon.tvfoot.red.PATH_MATCH
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.SCHEMES
import com.benoitquenaudon.tvfoot.red.app.common.BaseActivity
import com.benoitquenaudon.tvfoot.red.app.common.flowcontroller.FlowController
import com.benoitquenaudon.tvfoot.red.app.common.notification.MINUTES_BEFORE_NOTIFICATION
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match.MATCH_ID
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchIntent
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchStateBinder
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchViewState
import com.benoitquenaudon.tvfoot.red.app.domain.matches.BroadcastersAdapter
import com.benoitquenaudon.tvfoot.red.app.mvi.MviView
import com.benoitquenaudon.tvfoot.red.databinding.ActivityMatchBinding
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

class MatchActivity : BaseActivity(), MviView<MatchIntent, MatchViewState> {
  @Inject lateinit var broadcastersAdapter: BroadcastersAdapter
  @Inject lateinit var flowController: FlowController
  @Inject lateinit var stateBinder: MatchStateBinder
  @Inject lateinit var disposables: CompositeDisposable
  @Inject lateinit var viewModel: MatchViewModel

  private var binding: ActivityMatchBinding by Delegates.notNull<ActivityMatchBinding>()
  private var matchId: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityComponent.inject(this)

    val intent = intent
    matchId = intent.getStringExtra(MATCH_ID)

    val uri: Uri? = intent.data
    if (matchId == null && AUTHORITIES.contains(uri?.authority) && SCHEMES.contains(uri?.scheme)) {
      val segments = uri?.pathSegments
      if (segments?.size == 5 && PATH_MATCH == segments[0]) {
        matchId = segments[4]
      }
    }

    if (matchId == null) {
      Timber.w("matchDisplayable id is null %s", uri)
      Toast.makeText(this, "matchDisplayable id is null with uri $uri", Toast.LENGTH_LONG).show()
      flowController.toMatches()
      finish()
      return
    }

    setupView()

    Timber.d("matchDisplayable with load with id %s", matchId)
    bind()
  }

  private fun setupView() {
    binding = DataBindingUtil.setContentView<ActivityMatchBinding>(this, R.layout.activity_match)
    binding.matchDetailBroadcasters.adapter = broadcastersAdapter
    binding.viewModel = viewModel

    setSupportActionBar(binding.matchToolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  private fun bind() {
    disposables.add(stateBinder.states().subscribe(this::render))
    stateBinder.processIntents(intents())

    disposables.add(RxObservableBoolean.propertyChanges(viewModel.shouldNotifyMatchStart)
        .subscribe { shouldNotifyMatchStart ->
          if (shouldNotifyMatchStart) {
            Snackbar.make(binding.root,
                getString(R.string.will_be_notify_desc, MINUTES_BEFORE_NOTIFICATION),
                Snackbar.LENGTH_LONG).show()
          }
        })
  }

  override fun onDestroy() {
    super.onDestroy()
    disposables.dispose()
  }

  override fun intents(): Observable<MatchIntent> {
    return Observable.merge(initialIntent(), fabClickIntent())
  }

  private fun initialIntent(): Observable<MatchIntent.InitialIntent> {
    return Observable.just(MatchIntent.InitialIntent(checkNotNull(matchId, { "MatchId is null" })))
  }

  private fun fabClickIntent(): Observable<MatchIntent.NotifyMatchStartIntent> {
    return RxView.clicks(binding.notifyMatchStartFab)
        .map {
          MatchIntent.NotifyMatchStartIntent(viewModel.match.get().matchId(),
              viewModel.match.get().startAt(), !isMatchNotificationActivated)
        }
  }

  private val isMatchNotificationActivated: Boolean
    get() = binding.notifyMatchStartFab.isActivated

  override fun render(state: MatchViewState) = viewModel.updateFromState(state)
}
