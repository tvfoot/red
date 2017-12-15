package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

import com.benoitquenaudon.tvfoot.red.app.data.entity.Broadcaster
import com.benoitquenaudon.tvfoot.red.app.data.entity.Competition
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Team
import com.benoitquenaudon.tvfoot.red.util.MatchId
import com.benoitquenaudon.tvfoot.red.util.WillBeNotified
import com.benoitquenaudon.tvfoot.red.util.formatter
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

@Suppress("DataClassPrivateConstructor")
data class MatchRowDisplayable private constructor(
    val headerKey: String,
    val startTime: String,
    val broadcasters: List<BroadcasterRowDisplayable>,
    val headline: String,
    val competition: String,
    val competitionCode: String,
    val matchDay: String?,
    val live: Boolean,
    val homeTeamDrawableName: String,
    val awayTeamDrawableName: String,
    val location: String,
    val matchId: String,
    val tags: List<String>,
    val homeTeam: TeamRowDisplayable,
    val awayTeam: TeamRowDisplayable,
    val willBeNotified: Boolean = false,
    val startAt: Long
) : MatchesItemDisplayable, Comparable<MatchRowDisplayable> {
  override fun compareTo(other: MatchRowDisplayable): Int {
    return startAt.compareTo(other.startAt)
  }

  override fun isSameAs(other: MatchesItemDisplayable): Boolean {
    return other is MatchRowDisplayable && this.matchId == other.matchId
  }

  companion object Factory {
    private fun fromMatch(
        match: Match,
        willBeNotified: WillBeNotified
    ): MatchRowDisplayable {
      return MatchRowDisplayable(
          headerKey = parseHeaderKey(match.startAt),
          startTime = parseStartTime(match.startAt),
          broadcasters = parseBroadcasters(match.broadcasters),
          headline = parseHeadLine(match.homeTeam, match.awayTeam, match.label),
          competition = parseCompetition(match.competition),
          competitionCode = parseCompetitionCode(match.competition),
          matchDay = parseMatchDay(match.label, match.matchDay),
          live = isMatchLive(match.startAt),
          homeTeamDrawableName = parseHomeTeamDrawableName(match.homeTeam),
          awayTeamDrawableName = parseAwayTeamDrawableName(match.awayTeam),
          location = parseLocation(match),
          matchId = match.id,
          tags = parseTags(match),
          homeTeam = TeamRowDisplayable(
              match.homeTeam.name,
              match.homeTeam.logoPath,
              match.homeTeam.code),
          awayTeam = TeamRowDisplayable(
              match.awayTeam.name,
              match.awayTeam.logoPath,
              match.awayTeam.code),
          willBeNotified = willBeNotified,
          startAt = match.startAt.time
      )
    }

    fun fromMatches(
        matches: List<Match>,
        willBeNotifiedPairs: Map<MatchId, WillBeNotified>
    ): List<MatchRowDisplayable> {
      return matches.map {
        this.fromMatch(it, willBeNotifiedPairs.getOrElse(it.id, { false }))
      }
    }
  }
}

private val shortDateFormat: SimpleDateFormat by formatter("HH:mm")
val mediumDateFormat: SimpleDateFormat by formatter("yyyy-MM-dd")


private fun parseHeaderKey(startAt: Date): String = mediumDateFormat.format(startAt)

private fun parseStartTime(startAt: Date): String = shortDateFormat.format(startAt)

private fun parseBroadcasters(
    broadcasters: List<Broadcaster>?): List<BroadcasterRowDisplayable> {
  if (broadcasters == null) {
    return ArrayList()
  }

  return broadcasters
      .filter { it.name != null }
      .map { BroadcasterRowDisplayable(it.name!!, it.code) }
}

private fun parseHeadLine(homeTeam: Team, awayTeam: Team, matchLabel: String?): String {
  if (homeTeam.name.isNullOrEmpty() || awayTeam.name.isNullOrEmpty()) {
    return matchLabel ?: "undefined"
  }

  return homeTeam.name.toString().toUpperCase() +
      " - " +
      awayTeam.name.toString().toUpperCase()
}

private fun parseCompetition(competition: Competition): String = competition.name

private fun parseCompetitionCode(competition: Competition): String = competition.code

private fun parseMatchDay(matchLabel: String?, matchDay: String?): String? {
  if (matchLabel.isNullOrEmpty()) {
    if (matchDay.isNullOrEmpty()) return null // happens sometimes...

    val dayAsInt: Int? = matchDay?.toIntOrNull()
    if (dayAsInt == null) {
      return checkNotNull(matchDay)
    } else {
      return "J. $matchDay"
    }
  } else {
    return checkNotNull(matchLabel)
  }
}

private fun isMatchLive(startAt: Date): Boolean {
  val now = Calendar.getInstance().timeInMillis
  val startTimeInMillis = startAt.time

  return now in startTimeInMillis..(startTimeInMillis + TimeUnit.MINUTES.toMillis(105))
}

private fun parseHomeTeamDrawableName(homeTeam: Team): String = homeTeam.code ?: Team.DEFAULT_CODE

private fun parseAwayTeamDrawableName(awayTeam: Team): String = awayTeam.code ?: Team.DEFAULT_CODE

private fun parseLocation(match: Match): String = match.place.toString()

private fun parseTags(match: Match): List<String> =
    match.tags?.map(String::toLowerCase) ?: emptyList()