package com.benoitquenaudon.tvfoot.red.testutil;

import com.benoitquenaudon.tvfoot.red.api.TvfootService;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match;
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.Filter;
import io.reactivex.Single;
import java.util.List;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class FakeTvfootService implements TvfootService {
  private final Fixture fixture;

  public FakeTvfootService(Fixture fixture) {
    this.fixture = fixture;
  }

  @Override public Single<List<Match>> findFuture(@Query("filter") Filter filter) {
    if (filter.offset() == 0) return Single.just(fixture.anyMatches());
    if (filter.offset() > 0) return Single.just(fixture.anyNextMatches());
    throw new IllegalStateException("negative offset are not supposed to be");
  }

  @Override public Single<List<Match>> getMatches(@Query("filter") Filter filter) {
    throw new IllegalStateException("implement me");
  }

  @Override public Single<Match> getMatch(@Path("matchId") String matchId) {
    return Single.just(fixture.anyMatch());
  }
}
