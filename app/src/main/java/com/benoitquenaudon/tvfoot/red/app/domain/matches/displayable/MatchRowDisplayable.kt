package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

import com.benoitquenaudon.tvfoot.red.app.data.entity.Broadcaster
import com.benoitquenaudon.tvfoot.red.app.data.entity.Competition
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Team
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.LazyThreadSafetyMode.NONE

@Suppress("DataClassPrivateConstructor")
data class MatchRowDisplayable private constructor(
    val headerKey: String,
    val startTime: String,
    val broadcasters: List<BroadcasterRowDisplayable>,
    val headline: String,
    val competition: String,
    val matchDay: String,
    val live: Boolean,
    val homeTeamDrawableName: String,
    val awayTeamDrawableName: String,
    val location: String,
    val matchId: String
) : MatchesItemDisplayable {

  override fun isSameAs(newItem: MatchesItemDisplayable): Boolean {
    return newItem is MatchRowDisplayable && this.matchId == newItem.matchId
  }

  companion object Factory {
    private val shortDateFormat: DateFormat by lazy(NONE) {
      val format = SimpleDateFormat("HH:mm", Locale.getDefault())
      format.timeZone = TimeZone.getDefault()
      format
    }
    val mediumDateFormat: DateFormat by lazy(NONE) {
      val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
      format.timeZone = TimeZone.getDefault()
      format
    }

    fun fromMatch(match: Match): MatchRowDisplayable {
      return MatchRowDisplayable(
          parseHeaderKey(match.startAt()),
          parseStartTime(match.startAt()),
          parseBroadcasters(match.broadcasters()),
          parseHeadLine(match.homeTeam(), match.awayTeam(), match.label()),
          parseCompetition(match.competition()),
          parseMatchDay(match.label(), match.matchDay()),
          isMatchLive(match.startAt()),
          parseHomeTeamDrawableName(match.homeTeam()),
          parseAwayTeamDrawableName(match.awayTeam()),
          parseLocation(match),
          match.id())
    }

    fun fromMatches(matches: List<Match>): List<MatchRowDisplayable> = matches.map(this::fromMatch)

    private fun parseHeaderKey(startAt: Date): String = mediumDateFormat.format(startAt)

    private fun parseStartTime(startAt: Date): String = shortDateFormat.format(startAt)

    private fun parseBroadcasters(
        broadcasters: List<Broadcaster>?): List<BroadcasterRowDisplayable> {
      if (broadcasters == null) {
        return ArrayList()
      }

      return broadcasters.map { BroadcasterRowDisplayable.create(it.name(), it.code()) }
    }

    private fun parseHeadLine(homeTeam: Team, awayTeam: Team, matchLabel: String?): String {
      if (homeTeam.name().isNullOrEmpty() || awayTeam.name().isNullOrEmpty()) {
        return checkNotNull(matchLabel).toString()
      }

      return homeTeam.name().toString().toUpperCase() +
          " - " +
          awayTeam.name().toString().toUpperCase()
    }

    private fun parseCompetition(competition: Competition): String = competition.name()

    private fun parseMatchDay(matchLabel: String?, matchDay: String?): String {
      if (matchLabel.isNullOrEmpty()) {
        if (matchDay.isNullOrEmpty()) throw IllegalStateException("matchDay is empty")

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

    private fun parseHomeTeamDrawableName(homeTeam: Team): String {
      return homeTeam.code() ?: Team.DEFAULT_CODE
    }

    private fun parseAwayTeamDrawableName(awayTeam: Team): String {
      return awayTeam.code() ?: Team.DEFAULT_CODE
    }

    private fun parseLocation(match: Match): String = match.place().toString()
  }
}
