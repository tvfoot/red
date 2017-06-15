package com.benoitquenaudon.tvfoot.red.app.domain.matches.state;

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match;
import com.google.auto.value.AutoValue;
import java.util.List;
import javax.annotation.Nullable;

import static com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.Status.NEXT_PAGE_FAILURE;
import static com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.Status.NEXT_PAGE_IN_FLIGHT;
import static com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.Status.NEXT_PAGE_SUCCESS;
import static com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.Status.REFRESH_FAILURE;
import static com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.Status.REFRESH_IN_FLIGHT;
import static com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.Status.REFRESH_SUCCESS;

interface MatchesResult {
  @AutoValue abstract class RefreshResult implements MatchesResult {
    abstract Status status();

    @Nullable abstract List<Match> matches();

    @Nullable abstract Throwable throwable();

    static RefreshResult success(List<Match> matches) {
      return new AutoValue_MatchesResult_RefreshResult(REFRESH_SUCCESS, matches, null);
    }

    static RefreshResult failure(Throwable throwable) {
      return new AutoValue_MatchesResult_RefreshResult(REFRESH_FAILURE, null, throwable);
    }

    static RefreshResult inFlight() {
      return new AutoValue_MatchesResult_RefreshResult(REFRESH_IN_FLIGHT, null, null);
    }
  }

  @AutoValue abstract class GetLastStateResult implements MatchesResult {
    static GetLastStateResult create() {
      return new AutoValue_MatchesResult_GetLastStateResult();
    }
  }

  @AutoValue abstract class LoadNextPageResult implements MatchesResult {
    abstract Status status();

    @Nullable abstract List<Match> matches();

    @Nullable abstract Throwable error();

    abstract int pageIndex();

    static LoadNextPageResult success(int pageIndex, List<Match> matches) {
      return new AutoValue_MatchesResult_LoadNextPageResult(NEXT_PAGE_SUCCESS, matches, null,
          pageIndex);
    }

    static LoadNextPageResult failure(Throwable throwable) {
      return new AutoValue_MatchesResult_LoadNextPageResult(NEXT_PAGE_FAILURE, null, throwable, -1);
    }

    static LoadNextPageResult inFlight() {
      return new AutoValue_MatchesResult_LoadNextPageResult(NEXT_PAGE_IN_FLIGHT, null, null, -1);
    }
  }

  enum Status {
    REFRESH_IN_FLIGHT, REFRESH_FAILURE, REFRESH_SUCCESS, //
    NEXT_PAGE_IN_FLIGHT, NEXT_PAGE_FAILURE, NEXT_PAGE_SUCCESS, //
    PULL_TO_REFRESH_IN_FLIGHT, PULL_TO_REFRESH_FAILURE, PULL_TO_REFRESH_SUCCESS, //
  }
}
