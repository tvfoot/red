package io.oldering.tvfoot.red.app.domain.match.state;

import android.content.SharedPreferences;
import io.oldering.tvfoot.red.api.TvfootService;
import io.oldering.tvfoot.red.app.common.Notification;
import io.oldering.tvfoot.red.app.data.entity.Match;
import io.reactivex.Single;
import javax.inject.Inject;

public class MatchService {
  private static final String NOTIFY_MATCH_START = "NOTIFY_MATCH_START";
  private final TvfootService tvfootService;
  private final SharedPreferences preferences;

  @Inject MatchService(TvfootService tvfootService, SharedPreferences preferences) {
    this.tvfootService = tvfootService;
    this.preferences = preferences;
  }

  public Single<Match> loadMatch(String matchId) {
    return tvfootService.getMatch(matchId);
  }

  public Single<Notification> saveNotifyMatchStart(boolean notifyMatchStart) {
    preferences.edit().putBoolean(NOTIFY_MATCH_START, notifyMatchStart).apply();
    return Single.just(Notification.INSTANCE);
  }

  public Single<Boolean> loadNotifyMatchStart() {
    Single.just(preferences.getBoolean(NOTIFY_MATCH_START, false));
  }
}
