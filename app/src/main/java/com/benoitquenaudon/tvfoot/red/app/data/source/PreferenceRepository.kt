package com.benoitquenaudon.tvfoot.red.app.data.source

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification.INSTANCE
import com.benoitquenaudon.tvfoot.red.app.data.entity.FilterTeam
import com.benoitquenaudon.tvfoot.red.util.MatchId
import com.benoitquenaudon.tvfoot.red.util.TagName
import com.benoitquenaudon.tvfoot.red.util.TeamCode
import com.benoitquenaudon.tvfoot.red.util.flatMapIterable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject


class PreferenceRepository @Inject constructor(
    private val preferences: SharedPreferences
) : BasePreferenceRepository {
  override fun saveNotifyMatchStart(
      matchId: MatchId,
      notifyMatchStart: Boolean
  ): Single<StreamNotification> {
    if (notifyMatchStart) {
      addToBeNotifiedMatchId(matchId)
    } else {
      removeToBeNotifiedMatchId(matchId)
    }
    preferences.edit().putBoolean(notifyMatchStartPrefKey(matchId), notifyMatchStart).apply()
    return Single.just(INSTANCE)
  }

  override fun loadNotifyMatchStart(matchId: String): Single<Boolean> {
    return Single.just(preferences.getBoolean(notifyMatchStartPrefKey(matchId), false))
  }

  override fun loadFilteredBroadcasterNames(): Single<List<TagName>> {
    return Single.just(preferences.getStringSet(FILTERED_BROADCASTERS_KEY, emptySet()).toList())
  }

  override fun loadFilteredCompetitionNames(): Single<List<TagName>> {
    return Single.just(preferences.getStringSet(FILTERED_COMPETITIONS_KEY, emptySet()).toList())
  }

  override fun loadFilteredTeamCodes(): Single<List<TeamCode>> {
    return Single.just(preferences.getStringSet(FILTERED_TEAMS_KEY, emptySet()).toList())
  }

  private fun loadTeamCodes(): Single<List<TeamCode>> {
    return Single.just(preferences.getStringSet(TEAM_CODES_KEY, emptySet()).toList())
  }

  override fun loadTeams(): Single<List<FilterTeam>> {
    return loadTeamCodes()
        .flatMapIterable()
        .flatMapSingle { teamCode: TeamCode -> loadTeam(teamCode) }
        .toList()
        .map { it.asReversed() }
  }

  private fun loadTeam(teamCode: TeamCode): Single<FilterTeam> {
    return Single.just(
        FilterTeam(
            code = checkNotNull(preferences.getString(TEAM_CODE_KEY + teamCode, null)),
            name = checkNotNull(preferences.getString(TEAM_NAME_KEY + teamCode, null)),
            type = checkNotNull(preferences.getString(TEAM_TYPE_KEY + teamCode, null)),
            country = checkNotNull(preferences.getString(TEAM_COUNTRY_KEY + teamCode, null))
        )
    )
  }

  @SuppressLint("ApplySharedPref")
  override fun addTeam(filterTeam: FilterTeam): Single<StreamNotification> {
    val teamCode = filterTeam.code
    preferences.edit()
        .putString(TEAM_CODE_KEY + teamCode, filterTeam.code)
        .putString(TEAM_NAME_KEY + teamCode, filterTeam.name)
        .putString(TEAM_TYPE_KEY + teamCode, filterTeam.type)
        .putString(TEAM_COUNTRY_KEY + teamCode, filterTeam.country)
        .commit()

    saveTeamCodes((loadTeamCodes().blockingGet() + teamCode).toSet())

    return Single.just(INSTANCE)
  }

  @SuppressLint("ApplySharedPref")
  private fun saveTeamCodes(teamCodes: Set<TeamCode>) {
    preferences
        .edit()
        .putStringSet(TEAM_CODES_KEY, teamCodes)
        .commit()
  }

  override fun toggleFilteredBroadcasterName(broadcasterName: TagName): Single<StreamNotification> {
    val filteredBroadcasterNames = loadFilteredBroadcasterNames().blockingGet()
    if (broadcasterName in filteredBroadcasterNames) {
      saveFilteredBroadcasterNames((filteredBroadcasterNames - broadcasterName).toSet())
    } else {
      saveFilteredBroadcasterNames((filteredBroadcasterNames + broadcasterName).toSet())
    }
    return Single.just(INSTANCE)
  }

  override fun clearFilteredBroadcasterNames(): Single<StreamNotification> {
    saveFilteredBroadcasterNames(emptySet())
    return Single.just(INSTANCE)
  }

  override fun clearFilteredCompetitionNames(): Single<StreamNotification> {
    saveFilteredCompetitionNames(emptySet())
    return Single.just(INSTANCE)
  }

  override fun clearFilteredTeamCodes(): Single<StreamNotification> {
    saveFilteredTeamCodes(emptySet())
    return Single.just(INSTANCE)
  }

  @SuppressLint("ApplySharedPref")
  private fun saveFilteredBroadcasterNames(filteredBroadcastersNames: Set<TagName>) {
    preferences
        .edit()
        .putStringSet(FILTERED_BROADCASTERS_KEY, filteredBroadcastersNames)
        .commit()
  }

  override fun toggleFilteredCompetitionName(competitionName: TagName): Single<StreamNotification> {
    val filteredCompetitionNames = loadFilteredCompetitionNames().blockingGet()
    if (competitionName in filteredCompetitionNames) {
      saveFilteredCompetitionNames((filteredCompetitionNames - competitionName).toSet())
    } else {
      saveFilteredCompetitionNames((filteredCompetitionNames + competitionName).toSet())
    }
    return Single.just(INSTANCE)
  }

  @SuppressLint("ApplySharedPref")
  private fun saveFilteredCompetitionNames(filteredCompetitionsNames: Set<TagName>) {
    preferences
        .edit()
        .putStringSet(FILTERED_COMPETITIONS_KEY, filteredCompetitionsNames)
        .commit()
  }

  @SuppressLint("ApplySharedPref")
  private fun saveFilteredTeamCodes(filteredTeamCodes: Set<TeamCode>) {
    preferences
        .edit()
        .putStringSet(FILTERED_TEAMS_KEY, filteredTeamCodes)
        .commit()
  }

  override fun toggleFilteredTeamCode(teamCode: TeamCode): Single<StreamNotification> {
    val filteredTeamCodes = loadFilteredTeamCodes().blockingGet()
    if (teamCode in filteredTeamCodes) {
      saveFilteredTeamCodes((filteredTeamCodes - teamCode).toSet())
    } else {
      saveFilteredTeamCodes((filteredTeamCodes + teamCode).toSet())
    }
    return Single.just(INSTANCE)
  }

  fun loadToBeNotifiedMatchIds(): Observable<MatchId> {
    return Single
        .just(preferences.getStringSet(TO_BE_NOTIFIED_MATCH_IDS_KEY, emptySet()))
        .flatMapIterable()
  }

  private fun addToBeNotifiedMatchId(matchId: MatchId) {
    saveToBeNotifiedMatchIds(
        loadToBeNotifiedMatchIds().startWith(matchId).toList().blockingGet().toSet()
    )
  }

  private fun removeToBeNotifiedMatchId(matchId: MatchId) {
    saveToBeNotifiedMatchIds(
        loadToBeNotifiedMatchIds().filter { it != matchId }.toList().blockingGet().toSet()
    )
  }

  @SuppressLint("ApplySharedPref")
  private fun saveToBeNotifiedMatchIds(matchIds: Set<MatchId>) {
    preferences.edit().putStringSet(TO_BE_NOTIFIED_MATCH_IDS_KEY, matchIds).commit()
  }

  companion object {
    private val TO_BE_NOTIFIED_MATCH_IDS_KEY = "TO_BE_NOTIFIED_MATCH_IDS"
    private val FILTERED_COMPETITIONS_KEY = "FILTERED_COMPETITIONS_KEY"
    private val FILTERED_BROADCASTERS_KEY = "FILTERED_BROADCASTERS_KEY"
    private val FILTERED_TEAMS_KEY = "FILTERED_TEAMS_KEY"
    private val TEAM_CODES_KEY = "TEAM_CODES_KEY"
    private val TEAM_CODE_KEY = "TEAM_CODE_KEY_"
    private val TEAM_NAME_KEY = "TEAM_NAME_KEY_"
    private val TEAM_TYPE_KEY = "TEAM_TYPE_KEY_"
    private val TEAM_COUNTRY_KEY = "TEAM_COUNTRY_KEY_"

    private fun notifyMatchStartPrefKey(matchId: String): String {
      return "NOTIFY_MATCH_START_$matchId"
    }
  }
}