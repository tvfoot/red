package io.oldering.tvfoot.red.matches;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.data.model.Match;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoValue public abstract class MatchesViewState {
  public abstract List<Match> matches();

  public abstract boolean firstPageLoading();

  @Nullable public abstract Throwable firstPageError();

  public abstract boolean nextPageLoading();

  @Nullable public abstract Throwable nextPageError();

  public abstract boolean pullToRefreshLoading();

  @Nullable public abstract Throwable pullToRefreshError();

  public abstract Status status();

  public static Builder builder() {
    return new AutoValue_MatchesViewState.Builder().setMatches(Collections.emptyList())
        .setFirstPageLoading(false)
        .setFirstPageError(null)
        .setNextPageLoading(false)
        .setNextPageError(null)
        .setPullToRefreshLoading(false)
        .setPullToRefreshError(null);
  }

  public abstract Builder buildWith();

  public MatchesViewState reduce(MatchesViewState changes) {
    switch (changes.status()) {
      case FIRST_PAGE_LOADING:
        return this.buildWith()
            .setFirstPageLoading(true)
            .setFirstPageError(null)
            .setStatus(changes.status())
            .build();
      case FIRST_PAGE_ERROR:
        return this.buildWith()
            .setFirstPageLoading(false)
            .setFirstPageError(changes.firstPageError())
            .setStatus(changes.status())
            .build();
      case FIRST_PAGE_LOADED:
        return this.buildWith()
            .setFirstPageLoading(false)
            .setFirstPageError(null)
            .setMatches(changes.matches())
            .setStatus(changes.status())
            .build();
      case NEXT_PAGE_LOADING:
        return this.buildWith()
            .setNextPageLoading(true)
            .setNextPageError(null)
            .setStatus(changes.status())
            .build();
      case NEXT_PAGE_ERROR:
        return this.buildWith()
            .setNextPageLoading(false)
            .setNextPageError(changes.nextPageError())
            .setStatus(changes.status())
            .build();
      case NEXT_PAGE_LOADED:
        List<Match> matches = new ArrayList<>();
        matches.addAll(this.matches());
        matches.addAll(changes.matches());

        return this.buildWith()
            .setNextPageLoading(false)
            .setNextPageError(null)
            .setMatches(matches)
            .setStatus(changes.status())
            .build();
      case PULL_TO_REFRESH_LOADING:
        return this.buildWith()
            .setPullToRefreshLoading(true)
            .setPullToRefreshError(null)
            .setStatus(changes.status())
            .build();
      case PULL_TO_REFRESH_ERROR:
        return this.buildWith()
            .setPullToRefreshLoading(false)
            .setPullToRefreshError(changes.pullToRefreshError())
            .setStatus(changes.status())
            .build();
      case PULL_TO_REFRESH_LOADED:
        matches = new ArrayList<>();
        matches.addAll(changes.matches());
        matches.addAll(this.matches());

        return this.buildWith()
            .setPullToRefreshLoading(false)
            .setPullToRefreshError(null)
            .setMatches(matches)
            .setStatus(changes.status())
            .build();
      default:
        throw new IllegalArgumentException("Don't know this one " + changes);
    }
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder setMatches(List<Match> matches);

    public abstract Builder setFirstPageLoading(boolean firstPageLoading);

    public abstract Builder setFirstPageError(@Nullable Throwable error);

    public abstract Builder setNextPageLoading(boolean nextPageLoading);

    public abstract Builder setNextPageError(@Nullable Throwable error);

    public abstract Builder setPullToRefreshLoading(boolean pullToRefreshLoading);

    public abstract Builder setPullToRefreshError(@Nullable Throwable error);

    public abstract Builder setStatus(Status status);

    public abstract MatchesViewState build();
  }

  public enum Status {
    FIRST_PAGE_LOADING, FIRST_PAGE_ERROR, FIRST_PAGE_LOADED, //
    NEXT_PAGE_LOADING, NEXT_PAGE_ERROR, NEXT_PAGE_LOADED, //
    PULL_TO_REFRESH_LOADING, PULL_TO_REFRESH_ERROR, PULL_TO_REFRESH_LOADED, //
  }
}
