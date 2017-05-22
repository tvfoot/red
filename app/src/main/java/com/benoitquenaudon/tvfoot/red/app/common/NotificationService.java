package com.benoitquenaudon.tvfoot.red.app.common;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match;
import com.benoitquenaudon.tvfoot.red.app.domain.match.receiver.MatchReminderReceiver;
import io.reactivex.Single;
import javax.inject.Inject;

import static android.text.format.DateUtils.MINUTE_IN_MILLIS;

public class NotificationService {
  private final Context context;

  @Inject public NotificationService(Application context) {
    this.context = context;
  }

  public Single<Notification> manageNotification(String matchId, long startAt,
      boolean shouldNotify) {
    Intent intent = new Intent(context, MatchReminderReceiver.class);
    intent.putExtra(Match.MATCH_ID, matchId);
    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, matchIdAsInt(matchId), intent,
        PendingIntent.FLAG_CANCEL_CURRENT);

    if (shouldNotify) {
      alarmManager().setExact(AlarmManager.RTC_WAKEUP, startAt - 10 * MINUTE_IN_MILLIS,
          alarmIntent);
    } else {
      alarmManager().cancel(alarmIntent);
    }

    return Single.just(Notification.INSTANCE);
  }

  private AlarmManager alarmManager() {
    return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
  }

  private static int matchIdAsInt(String matchId) {
    //return Integer.parseInt(matchId(), 16);
    return Integer.parseInt(matchId.replaceAll("[^0-9]", "").substring(1, 10));
  }
}
