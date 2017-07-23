package com.benoitquenaudon.tvfoot.red.app.domain.match.job

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.benoitquenaudon.tvfoot.red.RedApp
import com.benoitquenaudon.tvfoot.red.app.common.PreferenceRepository
import com.benoitquenaudon.tvfoot.red.app.common.notification.MatchNotificationHelper
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchRepository
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject

class MatchReminderService : Service() {
  @Inject lateinit var disposables: CompositeDisposable
  @Inject lateinit var matchRepository: MatchRepository
  @Inject lateinit var preferenceRepository: PreferenceRepository
  @Inject lateinit var schedulerProvider: BaseSchedulerProvider

  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()
    val redApp = application as RedApp
    redApp.appComponent.inject(this)
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    val matchId = checkNotNull(intent.getStringExtra(Match.MATCH_ID)) { "matchId == null" }

    if (intent.action == null) {
      stopSelf()
      return Service.START_NOT_STICKY
    }

    val action: String = intent.action
    if (action == ACTION_PUBLISH_NOTIFICATION) {
      disposables.add(
          Single.zip<Match, Boolean, Pair<Match, Boolean>>(
              matchRepository.loadMatch(matchId),
              preferenceRepository.loadNotifyMatchStart(matchId),
              BiFunction<Match, Boolean, Pair<Match, Boolean>> { first, second ->
                Pair(first, second)
              }
          )
              .filter { (_, shouldNotify) -> shouldNotify }
              .map { (matchId) -> matchId }
              .subscribeOn(schedulerProvider.io())
              .doFinally { stopSelf(startId) }
              .subscribe(
                  { match ->
                    MatchNotificationHelper(applicationContext, match).publishMatchStarting()
                  },
                  { error -> Timber.e(error, "Could not display the game's notification") }
              )
      )
    } else {
      Timber.w("Don't know this action %s", action)
    }

    return Service.START_REDELIVER_INTENT
  }

  override fun onDestroy() {
    disposables.dispose()
    super.onDestroy()
  }

  companion object {
    val ACTION_PUBLISH_NOTIFICATION = "com.benoitquenaudon.tvfoot.red.action.PUBLISH_NOTIFICATION"
  }
}
