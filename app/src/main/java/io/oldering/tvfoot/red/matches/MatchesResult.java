package io.oldering.tvfoot.red.matches;

import io.oldering.tvfoot.red.data.entity.Match;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import java.util.List;

interface MatchesResult {
  class LoadFirstPageResult implements MatchesResult {
    private final Status status;
    private List<Match> matches;
    private Throwable throwable;

    public LoadFirstPageResult(List<Match> matches) {
      this.matches = matches;
      this.status = Status.FIRST_PAGE_SUCCESS;
    }

    public LoadFirstPageResult(Throwable throwable) {
      this.throwable = throwable;
      this.status = Status.FIRST_PAGE_FAILURE;
    }

    public Status getStatus() {
      return status;
    }

    public List<Match> getMatches() {
      return matches;
    }

    public Throwable getThrowable() {
      return throwable;
    }

    public LoadFirstPageResult() {
      this.status = Status.FIRST_PAGE_IN_FLIGHT;
    }

    public static LoadFirstPageResult success(List<Match> matches) {
      return new LoadFirstPageResult(matches);
    }

    public static LoadFirstPageResult failure(Throwable throwable) {
      return new LoadFirstPageResult(throwable);
    }

    public static LoadFirstPageResult inFlight() {
      return new LoadFirstPageResult();
    }
  }

  class LoadNextPageResult implements MatchesResult {
    private final Status status;
    private List<Match> matches;
    private Throwable throwable;

    public LoadNextPageResult(List<Match> matches) {
      this.matches = matches;
      this.status = Status.NEXT_PAGE_SUCCESS;
    }

    public LoadNextPageResult(Throwable throwable) {
      this.throwable = throwable;
      this.status = Status.NEXT_PAGE_FAILURE;
    }

    public Status getStatus() {
      return status;
    }

    public List<Match> getMatches() {
      return matches;
    }

    public Throwable getThrowable() {
      return throwable;
    }

    public LoadNextPageResult() {
      this.status = Status.NEXT_PAGE_IN_FLIGHT;
    }

    public static LoadNextPageResult success(List<Match> matches) {
      return new LoadNextPageResult(matches);
    }

    public static LoadNextPageResult failure(Throwable throwable) {
      return new LoadNextPageResult(throwable);
    }

    public static LoadNextPageResult inFlight() {
      return new LoadNextPageResult();
    }
  }

  class MatchRowClickResult implements MatchesResult {
    private final MatchRowDisplayable match;

    public MatchRowClickResult(MatchRowDisplayable match) {
      this.match = match;
    }

    public MatchRowDisplayable match() {
      return match;
    }
  }

  enum Status {
    FIRST_PAGE_IN_FLIGHT, FIRST_PAGE_FAILURE, FIRST_PAGE_SUCCESS, //
    NEXT_PAGE_IN_FLIGHT, NEXT_PAGE_FAILURE, NEXT_PAGE_SUCCESS, //
    PULL_TO_REFRESH_IN_FLIGHT, PULL_TO_REFRESH_FAILURE, PULL_TO_REFRESH_SUCCESS, //
    MATCH_ROW_CLICK
  }
}
