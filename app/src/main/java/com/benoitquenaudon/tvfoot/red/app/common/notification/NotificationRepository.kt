package com.benoitquenaudon.tvfoot.red.app.common.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import com.benoitquenaudon.tvfoot.red.RedApp
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.domain.match.job.MatchReminderService
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val context: RedApp,
    private val alarmManager: AlarmManager
) {
  fun scheduleNotification(
      matchId: String, startAt: Long, shouldNotify: Boolean
  ): Single<StreamNotification> {
    val intent = Intent(context, MatchReminderService::class.java)
        .setAction(MatchReminderService.ACTION_PUBLISH_NOTIFICATION)
        .putExtra(Match.MATCH_ID, matchId)

    val serviceIntent =
      if (Build.VERSION.SDK_INT >= 26) {
        PendingIntent.getForegroundService(
            context,
            matchIdAsInt(matchId),
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
      } else {
        PendingIntent.getService(
            context,
            matchIdAsInt(matchId),
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
      }

    if (shouldNotify) {
      alarmManager.setExact(
          AlarmManager.RTC_WAKEUP,
          startAt - TimeUnit.MINUTES.toMillis(MINUTES_BEFORE_NOTIFICATION.toLong()),
          serviceIntent)
    } else {
      alarmManager.cancel(serviceIntent)
    }

    return Single.just(StreamNotification.INSTANCE)
  }

  companion object {
    // MatchId are strings but the intent's ids are Integer so half ass solution for now
    fun matchIdAsInt(matchId: String): Int {
      //return Integer.parseInt(matchId(), 16);
      return Integer.parseInt(matchId.replace("[^0-9]".toRegex(), "").substring(1, 10))
    }
  }
}
