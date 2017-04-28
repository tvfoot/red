package io.oldering.tvfoot.red.matches.state;

import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.matches.displayable.HeaderRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.LoadingRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.MatchesItemDisplayable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

@AutoValue public abstract class MatchesViewState {
  public List<MatchesItemDisplayable> matchesItemDisplayables() {
    List<String> headers = new ArrayList<>();
    List<MatchesItemDisplayable> items = new ArrayList<>();
    for (MatchRowDisplayable match : matches()) {
      if (!headers.contains(match.headerKey())) {
        headers.add(match.headerKey());
        items.add(HeaderRowDisplayable.create(match.headerKey()));
      }
      items.add(match);
    }
    if (!items.isEmpty()) {
      items.add(LoadingRowDisplayable.create());
    }
    return items;
  }

  public abstract List<MatchRowDisplayable> matches();

  public abstract boolean firstPageLoading();

  @Nullable public abstract Throwable error();

  public abstract boolean nextPageLoading();

  public abstract boolean pullToRefreshLoading();

  @Nullable public abstract MatchRowDisplayable clickedMatch();

  public abstract Integer currentPage();

  public static Builder builder() {
    return new AutoValue_MatchesViewState.Builder().matches(Collections.emptyList())
        .firstPageLoading(false)
        .error(null)
        .nextPageLoading(false)
        .pullToRefreshLoading(false)
        .clickedMatch(null)
        .currentPage(0);
  }

  public abstract Builder buildWith();

  static MatchesViewState idle() {
    return MatchesViewState.builder().build();
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder matches(List<MatchRowDisplayable> matches);

    public abstract Builder firstPageLoading(boolean firstPageLoading);

    public abstract Builder error(@Nullable Throwable error);

    public abstract Builder nextPageLoading(boolean nextPageLoading);

    public abstract Builder pullToRefreshLoading(boolean pullToRefreshLoading);

    public abstract Builder clickedMatch(@Nullable MatchRowDisplayable clickedMatch);

    public abstract Builder currentPage(Integer CurrentPage);

    public abstract MatchesViewState build();
  }
}
