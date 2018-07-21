package com.benoitquenaudon.tvfoot.red.app.domain.match.job

import android.app.job.JobParameters
import android.app.job.JobService
import com.benoitquenaudon.tvfoot.red.app.common.notification.NotificationRepository
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.data.source.MatchRepository
import com.benoitquenaudon.tvfoot.red.app.data.source.PreferenceRepository
import com.benoitquenaudon.tvfoot.red.util.errorHandlingSubscribe
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class MatchNotificationSchedulerService : JobService() {
  @Inject
  lateinit var disposables: CompositeDisposable
  @Inject
  lateinit var matchRepository: MatchRepository
  @Inject
  lateinit var notificationRepository: NotificationRepository
  @Inject
  lateinit var preferenceRepository: PreferenceRepository
  @Inject
  lateinit var schedulerProvider: BaseSchedulerProvider

  private val nowOnCreate: Long = System.currentTimeMillis()

  override fun onCreate() {
    AndroidInjection.inject(this)
    super.onCreate()
  }

  override fun onStartJob(params: JobParameters?): Boolean {
    disposables.add(
        preferenceRepository.loadToBeNotifiedMatchIds()
            .flatMapSingle { matchId -> matchRepository.loadMatch(matchId) }
            .filter { it.startAt.time > nowOnCreate }
            .map { it.id to it.startAt.time }
            .flatMapSingle { (matchId, startAt) ->
              notificationRepository.scheduleNotification(matchId, startAt, true)
            }
            .doFinally { jobFinished(params, false) }
            .doOnError(Timber::e)
            .subscribeOn(schedulerProvider.io())
            .errorHandlingSubscribe())

    return true
  }

  override fun onStopJob(params: JobParameters?): Boolean {
    disposables.dispose()
    return true
  }

  companion object {
    const val MATCH_NOTIFICATION_SCHEDULER_JOB_ID = 806
  }
}