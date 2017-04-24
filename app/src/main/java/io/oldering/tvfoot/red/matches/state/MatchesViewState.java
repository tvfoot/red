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

import static io.oldering.tvfoot.red.matches.state.MatchesViewState.Status.IDLE;

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

  @Nullable public abstract Throwable throwable();

  public abstract boolean nextPageLoading();

  public abstract boolean pullToRefreshLoading();

  @Nullable public abstract MatchRowDisplayable match();

  public abstract Status status();

  public abstract Integer currentPage();

  public static Builder builder() {
    return new AutoValue_MatchesViewState.Builder().matches(Collections.emptyList())
        .firstPageLoading(false)
        .throwable(null)
        .nextPageLoading(false)
        .pullToRefreshLoading(false)
        .match(null)
        .currentPage(0);
  }

  public abstract Builder buildWith();

  static MatchesViewState idle() {
    return MatchesViewState.builder().status(IDLE).build();
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder matches(List<MatchRowDisplayable> matches);

    public abstract Builder firstPageLoading(boolean firstPageLoading);

    public abstract Builder throwable(@Nullable Throwable error);

    public abstract Builder nextPageLoading(boolean nextPageLoading);

    public abstract Builder pullToRefreshLoading(boolean pullToRefreshLoading);

    public abstract Builder status(Status status);

    public abstract Builder match(@Nullable MatchRowDisplayable match);

    public abstract Builder currentPage(Integer CurrentPage);

    public abstract MatchesViewState build();
  }

  public enum Status {
    FIRST_PAGE_IN_FLIGHT, FIRST_PAGE_FAILURE, FIRST_PAGE_SUCCESS, //
    NEXT_PAGE_IN_FLIGHT, NEXT_PAGE_FAILURE, NEXT_PAGE_SUCCESS, //
    MATCH_ROW_CLICK, //
    IDLE
  }
}
