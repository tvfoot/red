package com.benoitquenaudon.tvfoot.red.app.common.notification;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match;
import com.benoitquenaudon.tvfoot.red.app.domain.match.job.MatchReminderService;
import io.reactivex.Single;
import javax.inject.Inject;

import static android.text.format.DateUtils.MINUTE_IN_MILLIS;
import static com.benoitquenaudon.tvfoot.red.app.domain.match.job.MatchReminderService.ACTION_PUBLISH_NOTIFICATION;

public class NotificationService {
  private final Context context;
  private final AlarmManager alarmManager;

  @Inject public NotificationService(Application context, AlarmManager alarmManager) {
    this.context = context;
    this.alarmManager = alarmManager;
  }

  public Single<StreamNotification> scheduleNotification(String matchId, long startAt,
      boolean shouldNotify) {
    Intent intent = new Intent(context, MatchReminderService.class) //
        .setAction(ACTION_PUBLISH_NOTIFICATION) //
        .putExtra(Match.MATCH_ID, matchId);

    PendingIntent serviceIntent = PendingIntent.getService(context, matchIdAsInt(matchId), intent,
        PendingIntent.FLAG_CANCEL_CURRENT);

    if (shouldNotify) {
      alarmManager.setExact(AlarmManager.RTC_WAKEUP, startAt - 10 * MINUTE_IN_MILLIS,
          serviceIntent);
    } else {
      alarmManager.cancel(serviceIntent);
    }

    return Single.just(StreamNotification.INSTANCE);
  }

  static int matchIdAsInt(String matchId) {
    //return Integer.parseInt(matchId(), 16);
    return Integer.parseInt(matchId.replaceAll("[^0-9]", "").substring(1, 10));
  }
}
