package io.oldering.tvfoot.red.match;

import com.google.auto.value.AutoValue;

interface MatchIntent {
  @AutoValue abstract class LoadMatch implements MatchIntent {
    public abstract String matchId();

    public static MatchIntent.LoadMatch create(String matchId) {
      return new AutoValue_MatchIntent_LoadMatch(matchId);
    }
  }
}
