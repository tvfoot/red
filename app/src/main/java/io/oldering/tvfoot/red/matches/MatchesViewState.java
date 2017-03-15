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

  public abstract Builder toBuilder();

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
