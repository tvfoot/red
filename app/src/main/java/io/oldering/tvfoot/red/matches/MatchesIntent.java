package io.oldering.tvfoot.red.matches;

import android.util.Pair;
import android.widget.TextView;
import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;

interface MatchesIntent {
  @AutoValue abstract class LoadFirstPage implements MatchesIntent {
    public static LoadFirstPage create() {
      return new AutoValue_MatchesIntent_LoadFirstPage();
    }
  }

  @AutoValue abstract class LoadNextPage implements MatchesIntent {
    public abstract int currentPage();

    public static LoadNextPage create(int currentPage) {
      return new AutoValue_MatchesIntent_LoadNextPage(currentPage);
    }
  }

  @AutoValue abstract class MatchRowClick implements MatchesIntent {
    public abstract MatchRowDisplayable getMatch();

    public abstract TextView getHeadlineView();

    public static MatchRowClick create(Pair<MatchRowDisplayable, TextView> pair) {
      return new AutoValue_MatchesIntent_MatchRowClick(pair.first, pair.second);
    }
  }
}
