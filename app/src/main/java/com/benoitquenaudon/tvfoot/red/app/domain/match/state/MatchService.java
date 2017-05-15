package com.benoitquenaudon.tvfoot.red.app.domain.match.state;

import com.benoitquenaudon.tvfoot.red.api.TvfootService;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match;
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
