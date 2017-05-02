package io.oldering.tvfoot.red.app.domain.matches.state;

import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.app.data.entity.Match;
import java.util.List;
import javax.annotation.Nullable;

import static io.oldering.tvfoot.red.app.domain.matches.state.MatchesResult.Status.NEXT_PAGE_FAILURE;
import static io.oldering.tvfoot.red.app.domain.matches.state.MatchesResult.Status.NEXT_PAGE_IN_FLIGHT;
import static io.oldering.tvfoot.red.app.domain.matches.state.MatchesResult.Status.NEXT_PAGE_SUCCESS;
import static io.oldering.tvfoot.red.app.domain.matches.state.MatchesResult.Status.REFRESH_FAILURE;
import static io.oldering.tvfoot.red.app.domain.matches.state.MatchesResult.Status.REFRESH_IN_FLIGHT;
import static io.oldering.tvfoot.red.app.domain.matches.state.MatchesResult.Status.REFRESH_SUCCESS;

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

    abstract Integer pageIndex();

    static LoadNextPageResult success(List<Match> matches) {
      return new AutoValue_MatchesResult_LoadNextPageResult(NEXT_PAGE_SUCCESS, matches, null, -1);
    }

    static LoadNextPageResult failure(Throwable throwable) {
      return new AutoValue_MatchesResult_LoadNextPageResult(NEXT_PAGE_FAILURE, null, throwable, -1);
    }

    static LoadNextPageResult inFlight(int pageIndex) {
      return new AutoValue_MatchesResult_LoadNextPageResult(NEXT_PAGE_IN_FLIGHT, null, null,
          pageIndex);
    }
  }

  enum Status {
    REFRESH_IN_FLIGHT, REFRESH_FAILURE, REFRESH_SUCCESS, //
    NEXT_PAGE_IN_FLIGHT, NEXT_PAGE_FAILURE, NEXT_PAGE_SUCCESS, //
    PULL_TO_REFRESH_IN_FLIGHT, PULL_TO_REFRESH_FAILURE, PULL_TO_REFRESH_SUCCESS, //
  }
}
