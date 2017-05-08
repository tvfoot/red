package io.oldering.tvfoot.red.app.domain.match.state;

import io.oldering.tvfoot.red.api.TvfootService;
import io.oldering.tvfoot.red.app.data.entity.Match;
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
