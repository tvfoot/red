package com.benoitquenaudon.tvfoot.red.app.domain.matches.state;

import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.HeaderRowDisplayable;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.LoadingRowDisplayable;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchRowDisplayable;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayable;
import com.google.auto.value.AutoValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

@AutoValue public abstract class MatchesViewState {
  public List<MatchesItemDisplayable> matchesItemDisplayables(boolean hasMore) {
    List<String> headers = new ArrayList<>();
    List<MatchesItemDisplayable> items = new ArrayList<>();
    for (MatchRowDisplayable match : matches()) {
      if (!headers.contains(match.headerKey())) {
        headers.add(match.headerKey());
        items.add(HeaderRowDisplayable.create(match.headerKey()));
      }
      items.add(match);
    }
    // TODO(benoit) only add it when @param hasMore
    if (!items.isEmpty() && true) {
      items.add(LoadingRowDisplayable.create());
    }
    return items;
  }

  public abstract List<MatchRowDisplayable> matches();

  @Nullable public abstract Throwable error();

  public abstract boolean nextPageLoading();

  public abstract boolean refreshLoading();

  public abstract int currentPage();

  public abstract boolean hasMore();

  public static Builder builder() {
    return new AutoValue_MatchesViewState.Builder().matches(Collections.emptyList())
        .error(null)
        .nextPageLoading(false)
        .refreshLoading(false)
        .hasMore(true)
        .currentPage(0);
  }

  public abstract Builder buildWith();

  static MatchesViewState idle() {
    return MatchesViewState.builder().build();
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder matches(List<MatchRowDisplayable> matches);

    public abstract Builder error(@Nullable Throwable error);

    public abstract Builder nextPageLoading(boolean nextPageLoading);

    public abstract Builder refreshLoading(boolean pullToRefreshLoading);

    public abstract Builder currentPage(int CurrentPage);

    public abstract Builder hasMore(boolean hasMore);

    public abstract MatchesViewState build();
  }
}
