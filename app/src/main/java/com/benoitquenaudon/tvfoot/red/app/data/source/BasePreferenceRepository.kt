package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification
import com.benoitquenaudon.tvfoot.red.app.data.entity.FilterTeam
import com.benoitquenaudon.tvfoot.red.util.TagName
import com.benoitquenaudon.tvfoot.red.util.TeamCode
import io.reactivex.Single

interface BasePreferenceRepository {
  fun saveNotifyMatchStart(matchId: String, notifyMatchStart: Boolean): Single<StreamNotification>
  fun loadNotifyMatchStart(matchId: String): Single<Boolean>
  fun toggleFilteredBroadcasterName(broadcasterName: TagName): Single<StreamNotification>
  fun toggleFilteredCompetitionName(competitionName: TagName): Single<StreamNotification>
  fun toggleFilteredTeamCode(teamCode: TeamCode): Single<StreamNotification>
  fun loadFilteredBroadcasterNames(): Single<List<TagName>>
  fun loadFilteredCompetitionNames(): Single<List<TagName>>
  fun loadFilteredTeamCodes(): Single<List<TagName>>
  fun loadTeams(): Single<List<FilterTeam>>
  fun addTeam(filterTeam: FilterTeam): Single<StreamNotification>
  fun clearFilteredCompetitionNames(): Single<StreamNotification>
  fun clearFilteredBroadcasterNames(): Single<StreamNotification>
  fun clearFilteredTeamCodes(): Single<StreamNotification>
}