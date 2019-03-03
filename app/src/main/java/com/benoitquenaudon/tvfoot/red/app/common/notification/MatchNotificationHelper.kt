package com.benoitquenaudon.tvfoot.red.app.common.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchActivity
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchDisplayable

class MatchNotificationHelper(
    private val context: Context,
    private val match: Match
) {
  init {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannel(context)
    }
  }

  fun publishMatchStarting() {
    val intent = Intent(context, MatchActivity::class.java).apply {
      putExtra(Match.MATCH_ID, match.id)
    }

    val stackBuilder = TaskStackBuilder.create(context).apply {
      addParentStack(MatchActivity::class.java)
      addNextIntent(intent)
    }

    val pendingIntent = stackBuilder
        .getPendingIntent(NotificationRepository.matchIdAsInt(match.id),
            PendingIntent.FLAG_UPDATE_CURRENT)

    val matchDisplayable = MatchDisplayable.fromMatch(match)

    val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
        .setSmallIcon(R.drawable.logo)
        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .setContentTitle(matchDisplayable.headline)
        .setContentText(matchDisplayable.matchDay)
        .setSubText(matchDisplayable.competition)
        .setWhen(match.startAt.time)

    (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        .notify(NotificationRepository.matchIdAsInt(match.id), notificationBuilder.build())
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(NOTIFICATION_CHANNEL,
        context.getString(R.string.notification_match_starting_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT).also {
      it.setShowBadge(false)
    }
    (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        .createNotificationChannel(channel)
  }

  companion object Constant {
    const val NOTIFICATION_CHANNEL = "match_starting"
  }
}
