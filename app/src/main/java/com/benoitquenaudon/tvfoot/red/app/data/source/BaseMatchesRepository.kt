package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.util.MatchId
import com.benoitquenaudon.tvfoot.red.util.TeamCode
import com.benoitquenaudon.tvfoot.red.util.WillBeNotified
import io.reactivex.Single

interface BaseMatchesRepository {
  fun loadPage(pageIndex: Int): Single<List<Match>>
  fun loadTags(): Single<List<Tag>>
  fun searchTeamMatches(vararg codes: TeamCode): Single<List<Match>>
  fun loadPageWithNotificationStatus(
      pageIndex: Int
  ): Single<Pair<List<Match>, Map<MatchId, WillBeNotified>>>

  fun searchTeamMatchesWithNotificationStatus(
      vararg codes: TeamCode
  ): Single<Pair<List<Match>, Map<MatchId, WillBeNotified>>>
}