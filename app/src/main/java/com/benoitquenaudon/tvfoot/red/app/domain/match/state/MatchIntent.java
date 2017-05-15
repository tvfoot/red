package com.benoitquenaudon.tvfoot.red.app.domain.match.state;

import com.google.auto.value.AutoValue;

public interface MatchIntent {
  @AutoValue abstract class InitialIntent implements MatchIntent {
    public abstract String matchId();

    public static InitialIntent create(String matchId) {
      return new AutoValue_MatchIntent_InitialIntent(matchId);
    }
  }

  @AutoValue abstract class NotifyMatchStartIntent implements MatchIntent {
    public abstract String matchId();

    public abstract boolean notifyMatchStart();

    public static MatchIntent.NotifyMatchStartIntent create(String matchId,
        boolean notifyMatchStart) {
      return new AutoValue_MatchIntent_NotifyMatchStartIntent(matchId, notifyMatchStart);
    }
  }

  @AutoValue abstract class GetLastState implements MatchIntent {
    public static GetLastState create() {
      return new AutoValue_MatchIntent_GetLastState();
    }
  }
}
