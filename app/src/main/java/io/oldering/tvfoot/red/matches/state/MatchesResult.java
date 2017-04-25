package io.oldering.tvfoot.red.matches.state;

import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.data.entity.Match;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import java.util.List;
import javax.annotation.Nullable;

import static io.oldering.tvfoot.red.matches.state.MatchesResult.Status.FIRST_PAGE_FAILURE;
import static io.oldering.tvfoot.red.matches.state.MatchesResult.Status.FIRST_PAGE_IN_FLIGHT;
import static io.oldering.tvfoot.red.matches.state.MatchesResult.Status.FIRST_PAGE_SUCCESS;
import static io.oldering.tvfoot.red.matches.state.MatchesResult.Status.NEXT_PAGE_FAILURE;
import static io.oldering.tvfoot.red.matches.state.MatchesResult.Status.NEXT_PAGE_IN_FLIGHT;
import static io.oldering.tvfoot.red.matches.state.MatchesResult.Status.NEXT_PAGE_SUCCESS;

interface MatchesResult {
  @AutoValue abstract class LoadFirstPageResult implements MatchesResult {
    abstract Status status();

    @Nullable abstract List<Match> matches();

    @Nullable abstract Throwable throwable();

    static LoadFirstPageResult success(List<Match> matches) {
      return new AutoValue_MatchesResult_LoadFirstPageResult(FIRST_PAGE_SUCCESS, matches, null);
    }

    static LoadFirstPageResult failure(Throwable throwable) {
      return new AutoValue_MatchesResult_LoadFirstPageResult(FIRST_PAGE_FAILURE, null, throwable);
    }

    static LoadFirstPageResult inFlight() {
      return new AutoValue_MatchesResult_LoadFirstPageResult(FIRST_PAGE_IN_FLIGHT, null, null);
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

  @AutoValue abstract class MatchRowClickResult implements MatchesResult {
    abstract MatchRowDisplayable match();

    public static MatchRowClickResult create(MatchRowDisplayable match) {
      return new AutoValue_MatchesResult_MatchRowClickResult(match);
    }
  }

  enum Status {
    FIRST_PAGE_IN_FLIGHT, FIRST_PAGE_FAILURE, FIRST_PAGE_SUCCESS, //
    NEXT_PAGE_IN_FLIGHT, NEXT_PAGE_FAILURE, NEXT_PAGE_SUCCESS, //
    PULL_TO_REFRESH_IN_FLIGHT, PULL_TO_REFRESH_FAILURE, PULL_TO_REFRESH_SUCCESS, //
    MATCH_ROW_CLICK
  }
}
