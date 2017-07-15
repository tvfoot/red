package com.benoitquenaudon.tvfoot.red.api

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.Filter
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvfootService {
  companion object {
    val BASE_URL = "https://tvfoot.net"
  }

  // e.g.: filter:{"where":{"deleted":{"neq":1}},"order":"start-at ASC, weight ASC","limit":30}
  @GET("/api/matches/findFuture") //
  fun findFuture(@Query("filter") filter: Filter): Single<List<Match>>

  // e.g.: filter:{"where":{"start-at":{"gte":"2016-09-21T00:00:00.072Z"},"deleted":{"neq":1}},"order":"start-at ASC, weight ASC","limit":1500}
  @GET("/api/matches") //
  fun getMatches(@Query("filter") filter: Filter): Single<List<Match>>

  @GET("/api/matches/{matchId}") fun getMatch(@Path("matchId") matchId: String): Single<Match>
}