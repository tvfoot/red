package com.benoitquenaudon.tvfoot.red.app.domain.match.job;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import com.benoitquenaudon.tvfoot.red.RedApp;
import com.benoitquenaudon.tvfoot.red.app.common.PreferenceService;
import com.benoitquenaudon.tvfoot.red.app.common.notification.MatchNotificationHelper;
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match;
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchService;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Inject;
import timber.log.Timber;

import static com.benoitquenaudon.tvfoot.red.app.common.PreConditions.checkNotNull;

public class MatchReminderService extends Service {
  public static final String ACTION_PUBLISH_NOTIFICATION =
      "com.benoitquenaudon.tvfoot.red.action.PUBLISH_NOTIFICATION";

  @Inject CompositeDisposable disposables;
  @Inject MatchService matchService;
  @Inject PreferenceService preferenceService;
  @Inject BaseSchedulerProvider schedulerProvider;

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public void onCreate() {
    RedApp redApp = (RedApp) getApplication();
    redApp.getComponent().inject(this);
    super.onCreate();
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    final String matchId = checkNotNull(intent.getStringExtra(Match.MATCH_ID), "matchId == null");

    if (intent.getAction() == null) {
      stopSelf();
      return START_NOT_STICKY;
    }

    String action = intent.getAction();
    if (action.equals(ACTION_PUBLISH_NOTIFICATION)) {
      disposables.add(Single.zip(matchService.loadMatch(matchId),
          preferenceService.loadNotifyMatchStart(matchId), Pair::new)
          .filter(pair -> pair.second)
          .map(pair -> pair.first)
          .subscribeOn(schedulerProvider.io())
          .doFinally(() -> stopSelf(startId))
          .subscribe(match -> {
            new MatchNotificationHelper(getApplicationContext(), match).publishMatchStarting();
          }, error -> Timber.e(error, "Could not display the game's notification")));
    } else {
      Timber.w("Don't know this action %s", action);
    }

    return START_REDELIVER_INTENT;
  }

  @Override public void onDestroy() {
    super.onDestroy();
    disposables.dispose();
  }
}
