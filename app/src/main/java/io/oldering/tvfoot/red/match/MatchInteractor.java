package io.oldering.tvfoot.red.match;

import io.oldering.tvfoot.red.data.api.TvfootService;
import io.reactivex.Observable;
import javax.inject.Inject;

import static io.oldering.tvfoot.red.match.MatchViewState.Status.MATCH_ERROR;
import static io.oldering.tvfoot.red.match.MatchViewState.Status.MATCH_LOADED;
import static io.oldering.tvfoot.red.match.MatchViewState.Status.MATCH_LOADING;

public class MatchInteractor {
  private final TvfootService tvfootService;

  @Inject public MatchInteractor(TvfootService tvfootService) {
    this.tvfootService = tvfootService;
  }

  public Observable<MatchViewState> loadMatch(String matchId) {
    return tvfootService.getMatch(matchId)
        .toObservable()
        .map(MatchDisplayable::fromMatch)
        .map(match -> MatchViewState.builder().match(match).status(MATCH_LOADED).build())
        .startWith(MatchViewState.builder().status(MATCH_LOADING).build())
        .onErrorReturn(error -> MatchViewState.builder().error(error).status(MATCH_ERROR).build());
  }
}
