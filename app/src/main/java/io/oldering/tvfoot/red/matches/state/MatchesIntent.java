package io.oldering.tvfoot.red.matches.state;

import com.google.auto.value.AutoValue;

public interface MatchesIntent {
  @AutoValue abstract class InitialIntent implements MatchesIntent {
    public static InitialIntent create() {
      return new AutoValue_MatchesIntent_InitialIntent();
    }
  }

  @AutoValue abstract class GetLastState implements MatchesIntent {
    public static GetLastState create() {
      return new AutoValue_MatchesIntent_GetLastState();
    }
  }

  @AutoValue abstract class LoadNextPageIntent implements MatchesIntent {
    public abstract int pageIndex();

    public static LoadNextPageIntent create(int pageIndex) {
      return new AutoValue_MatchesIntent_LoadNextPageIntent(pageIndex);
    }
  }
}
