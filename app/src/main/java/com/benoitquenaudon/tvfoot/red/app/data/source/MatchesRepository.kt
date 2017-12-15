package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.api.TvfootService
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.Filter
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.TagsFilter
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.Where
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.Where.TeamCondition
import com.benoitquenaudon.tvfoot.red.injection.qualifiers.NowAtStartup
import com.benoitquenaudon.tvfoot.red.util.MatchId
import com.benoitquenaudon.tvfoot.red.util.TeamCode
import com.benoitquenaudon.tvfoot.red.util.WillBeNotified
import com.benoitquenaudon.tvfoot.red.util.flatMapIterable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.toMap
import javax.inject.Inject

class MatchesRepository @Inject constructor(
    private val tvfootService: TvfootService,
    private val preferenceRepository: BasePreferenceRepository,
    @NowAtStartup private val nowAtStartup: Long
) : BaseMatchesRepository {

  override fun loadPage(pageIndex: Int): Single<List<Match>> =
      tvfootService.getMatches(
          Filter(
              limit = MATCH_PER_PAGE,
              offset = MATCH_PER_PAGE * pageIndex,
              where = Where(startAt = nowAtStartup)))

  override fun loadPageWithNotificationStatus(
      pageIndex: Int
  ): Single<Pair<List<Match>, Map<MatchId, WillBeNotified>>> {
    val matches: Observable<Match> = loadPage(pageIndex).flatMapIterable().share()

    return Single.zip(
        matches.toList(),
        matches.map(Match::id)
            .flatMapSingle { matchId ->
              preferenceRepository
                  .loadNotifyMatchStart(matchId)
                  .map { matchId to it }
            }.toMap(),
        BiFunction<List<Match>,
            Map<MatchId, WillBeNotified>,
            Pair<List<Match>, Map<MatchId, WillBeNotified>>>(::Pair)
    )
  }

  override fun loadTags(): Single<List<Tag>> = tvfootService.getTags(TagsFilter())

  override fun searchTeamMatches(vararg codes: TeamCode): Single<List<Match>> =
      tvfootService.getMatches(
          Filter(
              limit = TEAM_SEARCH_LIMIT,
              offset = 0,
              where = Where(teams = codes.map(::TeamCondition), startAt = nowAtStartup)
          )
      )

  override fun searchTeamMatchesWithNotificationStatus(
      vararg codes: TeamCode
  ): Single<Pair<List<Match>, Map<MatchId, WillBeNotified>>> {
    val matches: Observable<Match> = searchTeamMatches(*codes).flatMapIterable().share()

    return Single.zip(
        matches.toList(),
        matches.map(Match::id)
            .flatMapSingle { matchId ->
              preferenceRepository
                  .loadNotifyMatchStart(matchId)
                  .map { matchId to it }
            }.toMap(),
        BiFunction<List<Match>,
            Map<MatchId, WillBeNotified>,
            Pair<List<Match>, Map<MatchId, WillBeNotified>>>(::Pair)
    )
  }

  companion object Constant {
    private val MATCH_PER_PAGE = 300
    private val TEAM_SEARCH_LIMIT = 1500
  }
}
