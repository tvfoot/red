package io.oldering.tvfoot.red.match.state;

import io.oldering.tvfoot.red.data.api.TvfootService;
import io.oldering.tvfoot.red.data.entity.Match;
import io.reactivex.Single;
import javax.inject.Inject;

public class MatchService {
  private final TvfootService tvfootService;

  @Inject MatchService(TvfootService tvfootService) {
    this.tvfootService = tvfootService;
  }

  public Single<Match> loadMatch(String matchId) {
    return tvfootService.getMatch(matchId);
  }
}
