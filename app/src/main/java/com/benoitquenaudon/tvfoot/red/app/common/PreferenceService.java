package com.benoitquenaudon.tvfoot.red.app.common;

import android.content.SharedPreferences;
import io.reactivex.Completable;
import io.reactivex.Single;
import javax.inject.Inject;

public class PreferenceService {
  private final SharedPreferences preferences;

  @Inject public PreferenceService(SharedPreferences preferences) {
    this.preferences = preferences;
  }

  public Completable saveNotifyMatchStart(String matchId, boolean notifyMatchStart) {
    preferences.edit().putBoolean(notifyMatchStartPrefKey(matchId), notifyMatchStart).apply();
    return Completable.complete();
  }

  public Single<Boolean> loadNotifyMatchStart(String matchId) {
    return Single.just(preferences.getBoolean(notifyMatchStartPrefKey(matchId), false));
  }

  private String notifyMatchStartPrefKey(String matchId) {
    return String.format("NOTIFY_MATCH_START_%s", matchId);
  }
}
