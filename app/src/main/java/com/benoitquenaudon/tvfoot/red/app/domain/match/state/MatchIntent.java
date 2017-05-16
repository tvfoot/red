package com.benoitquenaudon.tvfoot.red.app.domain.match.state;

import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchDisplayable;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

public interface MatchIntent {
  @AutoValue abstract class InitialIntent implements MatchIntent {
    @Nullable public abstract MatchDisplayable match();

    @Nullable public abstract String matchId();

    public static InitialIntent withMatchId(String matchId) {
      return new AutoValue_MatchIntent_InitialIntent(null, matchId);
    }

    public static InitialIntent withMatch(MatchDisplayable match) {
      return new AutoValue_MatchIntent_InitialIntent(match, null);
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
