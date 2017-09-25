package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

import com.benoitquenaudon.tvfoot.red.app.data.entity.Broadcaster
import com.benoitquenaudon.tvfoot.red.app.data.entity.Competition
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Team
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
    val awayTeam: TeamRowDisplayable
) : MatchesItemDisplayable {
  override fun isSameAs(newItem: MatchesItemDisplayable): Boolean {
    return newItem is MatchRowDisplayable && this.matchId == newItem.matchId
  }

  companion object Factory {
    fun fromMatch(match: Match): MatchRowDisplayable =
        MatchRowDisplayable(
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
            homeTeam = TeamRowDisplayable(match.homeTeam.name, parseTeamLogoPath(match.homeTeam)),
            awayTeam = TeamRowDisplayable(match.awayTeam.name, parseTeamLogoPath(match.awayTeam))
        )

    fun fromMatches(matches: List<Match>): List<MatchRowDisplayable> = matches.map(this::fromMatch)
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

  return broadcasters.map { BroadcasterRowDisplayable.create(it.name, it.code) }
}

private fun parseHeadLine(homeTeam: Team, awayTeam: Team, matchLabel: String?): String {
  if (homeTeam.name.isNullOrEmpty() || awayTeam.name.isNullOrEmpty()) {
    return checkNotNull(matchLabel).toString()
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

private fun parseTeamLogoPath(team: Team): String =
    if (team.code == null || team.type == null) {
      "/images/teams/default/large/default.png"
    } else {
      val type = checkNotNull(team.type) { "team's type should not be null" }
      val code = checkNotNull(team.code) { "team's code should not be null" }
      when (type) {
        "nation" -> String.format("/images/teams/nations/large/%s.png", code.toLowerCase())
        "club" -> {
          val country = checkNotNull(team.country) { "team's country should not be null" }
          "/images/teams/$country/large/${code.toLowerCase()}.png"
        }
        else -> throw IllegalStateException("Unknown type " + type)
      }
    }

private fun parseHomeTeamDrawableName(homeTeam: Team): String = homeTeam.code ?: Team.DEFAULT_CODE

private fun parseAwayTeamDrawableName(awayTeam: Team): String = awayTeam.code ?: Team.DEFAULT_CODE

private fun parseLocation(match: Match): String = match.place.toString()

private fun parseTags(match: Match): List<String> =
    match.tags?.map(String::toLowerCase) ?: emptyList()