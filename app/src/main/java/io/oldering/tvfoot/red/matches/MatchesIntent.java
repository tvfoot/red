package io.oldering.tvfoot.red.matches;

import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;

interface MatchesIntent {
  @AutoValue abstract class LoadFirstPageIntent implements MatchesIntent {
    public static LoadFirstPageIntent create() {
      return new AutoValue_MatchesIntent_LoadFirstPageIntent();
    }
  }

  @AutoValue abstract class LoadNextPageIntent implements MatchesIntent {
    public abstract int currentPage();

    public static LoadNextPageIntent create(int currentPage) {
      return new AutoValue_MatchesIntent_LoadNextPageIntent(currentPage);
    }
  }

  @AutoValue abstract class MatchRowClickIntent implements MatchesIntent {
    public abstract MatchRowDisplayable match();

    public static MatchRowClickIntent create(MatchRowDisplayable match) {
      return new AutoValue_MatchesIntent_MatchRowClickIntent(match);
    }
  }
}
