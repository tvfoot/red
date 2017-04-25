package io.oldering.tvfoot.red.match.state;

import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.data.entity.Match;
import javax.annotation.Nullable;

import static io.oldering.tvfoot.red.match.state.MatchResult.Status.LOAD_MATCH_FAILURE;
import static io.oldering.tvfoot.red.match.state.MatchResult.Status.LOAD_MATCH_IN_FLIGHT;
import static io.oldering.tvfoot.red.match.state.MatchResult.Status.LOAD_MATCH_SUCCESS;

interface MatchResult {
  @AutoValue abstract class LoadMatchResult implements MatchResult {
    abstract Status status();

    @Nullable abstract Match match();

    @Nullable abstract Throwable error();

    static LoadMatchResult success(Match match) {
      return new AutoValue_MatchResult_LoadMatchResult(LOAD_MATCH_SUCCESS, match, null);
    }

    static LoadMatchResult failure(Throwable throwable) {
      return new AutoValue_MatchResult_LoadMatchResult(LOAD_MATCH_FAILURE, null, throwable);
    }

    static LoadMatchResult inFlight() {
      return new AutoValue_MatchResult_LoadMatchResult(LOAD_MATCH_IN_FLIGHT, null, null);
    }
  }

  @AutoValue abstract class GetLastStateResult implements MatchResult {
    static GetLastStateResult create() {
      return new AutoValue_MatchResult_GetLastStateResult();
    }
  }

  enum Status {
    LOAD_MATCH_IN_FLIGHT, LOAD_MATCH_FAILURE, LOAD_MATCH_SUCCESS, //
  }
}
