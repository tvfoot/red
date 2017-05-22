package com.benoitquenaudon.tvfoot.red.app.domain.match.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match;

import static com.benoitquenaudon.tvfoot.red.app.common.PreConditions.checkNotNull;

public class MatchReminderReceiver extends BroadcastReceiver {
  @Override public void onReceive(Context context, Intent intent) {
    String matchId = checkNotNull(intent.getStringExtra(Match.MATCH_ID), "matchId == null");
    // load match
    // build notif
    // roll ?
  }
}
