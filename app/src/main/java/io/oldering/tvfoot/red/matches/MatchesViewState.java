package io.oldering.tvfoot.red.matches;

import android.support.annotation.VisibleForTesting;
import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.matches.displayable.HeaderRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.LoadingRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.MatchesItemDisplayable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.MATCH_ROW_CLICK;

@AutoValue public abstract class MatchesViewState {

  List<MatchesItemDisplayable> matchesItemDisplayables() {
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

  @VisibleForTesting protected abstract List<MatchRowDisplayable> matches();

  public abstract boolean firstPageLoading();

  @Nullable public abstract Throwable firstPageError();

  public abstract boolean nextPageLoading();

  @Nullable public abstract Throwable nextPageError();

  public abstract boolean pullToRefreshLoading();

  @Nullable public abstract Throwable pullToRefreshError();

  @Nullable public abstract MatchRowDisplayable match();

  public abstract Status status();

  public static Builder builder() {
    return new AutoValue_MatchesViewState.Builder().matches(Collections.emptyList())
        .firstPageLoading(false)
        .firstPageError(null)
        .nextPageLoading(false)
        .nextPageError(null)
        .pullToRefreshLoading(false)
        .pullToRefreshError(null)
        .match(null);
  }

  public abstract Builder buildWith();

  static MatchesViewState reduce(MatchesViewState previousState, MatchesViewState partialState) {
    switch (partialState.status()) {
      case FIRST_PAGE_LOADING:
        return previousState.buildWith()
            .firstPageLoading(true)
            .firstPageError(null)
            .status(partialState.status())
            .build();
      case FIRST_PAGE_ERROR:
        return previousState.buildWith()
            .firstPageLoading(false)
            .firstPageError(partialState.firstPageError())
            .status(partialState.status())
            .build();
      case FIRST_PAGE_LOADED:
        return previousState.buildWith()
            .firstPageLoading(false)
            .firstPageError(null)
            .matches(partialState.matches())
            .status(partialState.status())
            .build();
      case NEXT_PAGE_LOADING:
        return previousState.buildWith()
            .nextPageLoading(true)
            .nextPageError(null)
            .status(partialState.status())
            .build();
      case NEXT_PAGE_ERROR:
        return previousState.buildWith()
            .nextPageLoading(false)
            .nextPageError(partialState.nextPageError())
            .status(partialState.status())
            .build();
      case NEXT_PAGE_LOADED:
        List<MatchRowDisplayable> matches = new ArrayList<>();
        matches.addAll(previousState.matches());
        matches.addAll(partialState.matches());

        return previousState.buildWith()
            .nextPageLoading(false)
            .nextPageError(null)
            .matches(matches)
            .status(partialState.status())
            .build();
      case PULL_TO_REFRESH_LOADING:
        return previousState.buildWith()
            .pullToRefreshLoading(true)
            .pullToRefreshError(null)
            .status(partialState.status())
            .build();
      case PULL_TO_REFRESH_ERROR:
        return previousState.buildWith()
            .pullToRefreshLoading(false)
            .pullToRefreshError(partialState.pullToRefreshError())
            .status(partialState.status())
            .build();
      case PULL_TO_REFRESH_LOADED:
        matches = new ArrayList<>();
        matches.addAll(partialState.matches());
        matches.addAll(previousState.matches());

        return previousState.buildWith()
            .pullToRefreshLoading(false)
            .pullToRefreshError(null)
            .matches(matches)
            .status(partialState.status())
            .build();
      case MATCH_ROW_CLICK:
        return partialState;
      default:
        throw new IllegalArgumentException("Don't know this one " + partialState);
    }
  }

  static MatchesViewState matchRowClick(MatchRowDisplayable match) {
    return MatchesViewState.builder().match(match).status(MATCH_ROW_CLICK).build();
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder matches(List<MatchRowDisplayable> matches);

    public abstract Builder firstPageLoading(boolean firstPageLoading);

    public abstract Builder firstPageError(@Nullable Throwable error);

    public abstract Builder nextPageLoading(boolean nextPageLoading);

    public abstract Builder nextPageError(@Nullable Throwable error);

    public abstract Builder pullToRefreshLoading(boolean pullToRefreshLoading);

    public abstract Builder pullToRefreshError(@Nullable Throwable error);

    public abstract Builder status(Status status);

    public abstract Builder match(@Nullable MatchRowDisplayable match);

    public abstract MatchesViewState build();
  }

  public enum Status {
    FIRST_PAGE_LOADING, FIRST_PAGE_ERROR, FIRST_PAGE_LOADED, //
    NEXT_PAGE_LOADING, NEXT_PAGE_ERROR, NEXT_PAGE_LOADED, //
    PULL_TO_REFRESH_LOADING, PULL_TO_REFRESH_ERROR, PULL_TO_REFRESH_LOADED, //
    MATCH_ROW_CLICK
  }
}
