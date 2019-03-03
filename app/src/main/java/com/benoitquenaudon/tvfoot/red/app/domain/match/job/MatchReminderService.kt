package com.benoitquenaudon.tvfoot.red.app.domain.match.job

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.R.string
import com.benoitquenaudon.tvfoot.red.app.common.flowcontroller.FlowIntentFactory.toMatchesIntent
import com.benoitquenaudon.tvfoot.red.app.common.notification.MatchNotificationHelper
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.source.BaseMatchRepository
import com.benoitquenaudon.tvfoot.red.app.data.source.PreferenceRepository
import com.benoitquenaudon.tvfoot.red.util.errorHandlingSubscribe
import dagger.android.DaggerService
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject

class MatchReminderService : DaggerService() {
  @Inject lateinit var matchRepository: BaseMatchRepository
  @Inject lateinit var preferenceRepository: PreferenceRepository
  @Inject lateinit var schedulerProvider: BaseSchedulerProvider
  @Inject lateinit var disposables: CompositeDisposable

  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val pendingIntent: PendingIntent = toMatchesIntent().let { notificationIntent ->
        PendingIntent.getActivity(this, 0, notificationIntent, 0)
      }

      val channel =
        NotificationChannel(
            MatchNotificationHelper.NOTIFICATION_CHANNEL,
            this.getString(string.notification_match_starting_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
          setShowBadge(false)
        }

      this.getSystemService(NotificationManager::class.java)
          .createNotificationChannel(channel)

      val notification: Notification =
        Notification.Builder(this, MatchNotificationHelper.NOTIFICATION_CHANNEL)
            .setContentTitle(getString(R.string.loading_info))
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

      (this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
          .notify(FOREGROUND_NOTIFICATION_ID, notification)

      startForeground(FOREGROUND_NOTIFICATION_ID, notification)
    }
  }

  override fun onStartCommand(
    intent: Intent,
    flags: Int,
    startId: Int
  ): Int {
    val matchId = checkNotNull(intent.getStringExtra(Match.MATCH_ID)) { "matchId == null" }

    if (intent.action == null) {
      stopSelf()
      return Service.START_NOT_STICKY
    }

    val action: String? = intent.action
    if (action == ACTION_PUBLISH_NOTIFICATION) {
      disposables.add(
          Single
              .zip<Match, Boolean, Pair<Match, Boolean>>(
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
              .errorHandlingSubscribe { match ->
                MatchNotificationHelper(applicationContext, match).publishMatchStarting()
              }
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
    const val ACTION_PUBLISH_NOTIFICATION =
      "com.benoitquenaudon.tvfoot.red.action.PUBLISH_NOTIFICATION"
    const val FOREGROUND_NOTIFICATION_ID = 33
  }
}
