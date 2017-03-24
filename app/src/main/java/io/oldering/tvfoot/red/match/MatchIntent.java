package io.oldering.tvfoot.red.match;

import com.google.auto.value.AutoValue;

interface MatchIntent {
  @AutoValue abstract class LoadMatch implements MatchIntent {
    public static MatchIntent.LoadMatch create() {
      return new AutoValue_MatchIntent_LoadMatch();
    }
  }
}
