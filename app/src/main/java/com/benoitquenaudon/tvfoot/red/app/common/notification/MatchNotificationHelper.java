package com.benoitquenaudon.tvfoot.red.app.common.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import com.benoitquenaudon.tvfoot.red.R;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match;
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchActivity;
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchDisplayable;

import static com.benoitquenaudon.tvfoot.red.app.common.notification.NotificationService.matchIdAsInt;
import static com.benoitquenaudon.tvfoot.red.app.data.entity.Match.MATCH_ID;

public final class MatchNotificationHelper {
  private final Context context;
  private final Match match;

  public MatchNotificationHelper(Context context, Match match) {
    this.context = context;
    this.match = match;
  }

  public void publishMatchStarting() {
    Intent intent = new Intent(context, MatchActivity.class);
    intent.putExtra(MATCH_ID, match.id());
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addParentStack(MatchActivity.class);
    stackBuilder.addNextIntent(intent);

    PendingIntent pendingIntent =
        stackBuilder.getPendingIntent(matchIdAsInt(match.id()), PendingIntent.FLAG_UPDATE_CURRENT);

    MatchDisplayable matchDisplayable = MatchDisplayable.fromMatch(match);

    NotificationCompat.Builder notificationBuilder =
        new NotificationCompat.Builder(context).setSmallIcon(R.drawable.logo)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setContentTitle(matchDisplayable.headline())
            .setContentText(matchDisplayable.matchDay())
            .setSubText(matchDisplayable.competition())
            .setWhen(match.startAt().getTime());

    NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(matchIdAsInt(match.id()), notificationBuilder.build());
  }
}
