package io.oldering.tvfoot.red.matches;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.MATCH_ROW_CLICK;

@AutoValue public abstract class MatchesViewState {
  public abstract List<MatchRowDisplayable> matches();

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

  public MatchesViewState reduce(MatchesViewState changes) {
    switch (changes.status()) {
      case FIRST_PAGE_LOADING:
        return this.buildWith()
            .firstPageLoading(true)
            .firstPageError(null)
            .status(changes.status())
            .build();
      case FIRST_PAGE_ERROR:
        return this.buildWith()
            .firstPageLoading(false)
            .firstPageError(changes.firstPageError())
            .status(changes.status())
            .build();
      case FIRST_PAGE_LOADED:
        return this.buildWith()
            .firstPageLoading(false)
            .firstPageError(null)
            .matches(changes.matches())
            .status(changes.status())
            .build();
      case NEXT_PAGE_LOADING:
        return this.buildWith()
            .nextPageLoading(true)
            .nextPageError(null)
            .status(changes.status())
            .build();
      case NEXT_PAGE_ERROR:
        return this.buildWith()
            .nextPageLoading(false)
            .nextPageError(changes.nextPageError())
            .status(changes.status())
            .build();
      case NEXT_PAGE_LOADED:
        List<MatchRowDisplayable> matches = new ArrayList<>();
        matches.addAll(this.matches());
        matches.addAll(changes.matches());

        return this.buildWith()
            .nextPageLoading(false)
            .nextPageError(null)
            .matches(matches)
            .status(changes.status())
            .build();
      case PULL_TO_REFRESH_LOADING:
        return this.buildWith()
            .pullToRefreshLoading(true)
            .pullToRefreshError(null)
            .status(changes.status())
            .build();
      case PULL_TO_REFRESH_ERROR:
        return this.buildWith()
            .pullToRefreshLoading(false)
            .pullToRefreshError(changes.pullToRefreshError())
            .status(changes.status())
            .build();
      case PULL_TO_REFRESH_LOADED:
        matches = new ArrayList<>();
        matches.addAll(changes.matches());
        matches.addAll(this.matches());

        return this.buildWith()
            .pullToRefreshLoading(false)
            .pullToRefreshError(null)
            .matches(matches)
            .status(changes.status())
            .build();
      case MATCH_ROW_CLICK:
        return changes;
      default:
        throw new IllegalArgumentException("Don't know this one " + changes);
    }
  }

  public static MatchesViewState matchRowClick(MatchRowDisplayable match) {
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

    public abstract Builder match(MatchRowDisplayable match);

    public abstract MatchesViewState build();
  }

  public enum Status {
    FIRST_PAGE_LOADING, FIRST_PAGE_ERROR, FIRST_PAGE_LOADED, //
    NEXT_PAGE_LOADING, NEXT_PAGE_ERROR, NEXT_PAGE_LOADED, //
    PULL_TO_REFRESH_LOADING, PULL_TO_REFRESH_ERROR, PULL_TO_REFRESH_LOADED, //
    MATCH_ROW_CLICK
  }
}
