package io.oldering.tvfoot.red.util;

import io.oldering.tvfoot.red.data.api.MatchService;
import io.oldering.tvfoot.red.data.entity.Match;
import io.oldering.tvfoot.red.data.entity.search.Filter;
import io.reactivex.Single;
import java.util.List;
import retrofit2.http.Query;

public class FakeMatchService implements MatchService {
  private final Fixture fixture;

  public FakeMatchService(Fixture fixture) {
    this.fixture = fixture;
  }

  @Override public Single<List<Match>> findFuture(@Query("filter") Filter filter) {
    if (filter.offset() == 0) return Single.just(fixture.anyMatches());
    if (filter.offset() > 0) return Single.just(fixture.anyNextMatches());
    throw new IllegalStateException("negative offset are not supposed to be");
  }

  @Override public Single<List<Match>> get(@Query("filter") Filter filter) {
    throw new IllegalStateException("implement me");
  }
}
