package com.benoitquenaudon.tvfoot.red.app.domain.match.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import timber.log.Timber;

public class MatchReminderReceiver extends BroadcastReceiver {
  @Override public void onReceive(Context context, Intent intent) {
    //intent.setExtrasClassLoader(MatchDisplayable.class.getClassLoader());
    Timber.d("connard received stuff");
  }
}
