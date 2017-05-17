package com.benoitquenaudon.tvfoot.red.app.common;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.benoitquenaudon.tvfoot.red.app.domain.match.receiver.MatchReminderReceiver;
import javax.inject.Inject;
import timber.log.Timber;

public class NotificationService {
  private final Context context;

  @Inject public NotificationService(Application context) {
    this.context = context;
  }

  public void manageNotification(String matchId, long startAt, boolean shouldNotify) {
    Intent intent = new Intent(context, MatchReminderReceiver.class);
    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, matchIdAsInt(matchId), intent,
        PendingIntent.FLAG_CANCEL_CURRENT);

    Timber.d("connard doing stuff %s", shouldNotify);
    if (shouldNotify) {
      alarmManager().setExact(AlarmManager.RTC_WAKEUP, startAt, alarmIntent);
    } else {
      alarmManager().cancel(alarmIntent);
    }
  }

  private AlarmManager alarmManager() {
    return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
  }

  private static int matchIdAsInt(String matchId) {
    //return Integer.parseInt(matchId(), 16);
    return Integer.parseInt(matchId.replaceAll("[^0-9]", "").substring(1, 10));
  }
}
