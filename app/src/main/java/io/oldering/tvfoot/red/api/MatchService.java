package io.oldering.tvfoot.red.api;


import java.util.List;

import io.oldering.tvfoot.red.model.Match;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MatchService {
    String BASE_URL = "https://tvfoot.net";

    // e.g.: filter:{"where":{"deleted":{"neq":1}},"order":"start-at ASC, weight ASC","limit":30}
    @GET("/api/matches/findFuture")
    Single<List<Match>> findFuture(@Query("filter") String filter);

    // e.g.: filter:{"where":{"start-at":{"gte":"2016-09-21T00:00:00.072Z"},"deleted":{"neq":1}},"order":"start-at ASC, weight ASC","limit":1500}
    @GET("/api/matches")
    Single<List<Match>> get(@Query("filter") String filter);

}