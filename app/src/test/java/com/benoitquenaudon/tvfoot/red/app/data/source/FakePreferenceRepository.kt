package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification.INSTANCE
import com.benoitquenaudon.tvfoot.red.app.data.entity.FilterTeam
import com.benoitquenaudon.tvfoot.red.util.TagName
import com.benoitquenaudon.tvfoot.red.util.TeamCode
import io.reactivex.Single

class FakePreferenceRepository : BasePreferenceRepository {
  override fun saveNotifyMatchStart(
      matchId: String,
      notifyMatchStart: Boolean
  ): Single<StreamNotification> {
    return Single.just(INSTANCE)
  }

  override fun loadNotifyMatchStart(matchId: String): Single<Boolean> {
    return Single.just(false)
  }

  override fun loadFilteredBroadcasterNames(): Single<List<TagName>> {
    return Single.just(emptyList())
  }

  override fun loadFilteredCompetitionNames(): Single<List<TagName>> {
    return Single.just(emptyList())
  }

  override fun toggleFilteredBroadcasterName(broadcasterName: TagName): Single<StreamNotification> {
    return Single.just(INSTANCE)
  }

  override fun toggleFilteredCompetitionName(competitionName: TagName): Single<StreamNotification> {
    return Single.just(INSTANCE)
  }

  override fun clearFilteredBroadcasterNames(): Single<StreamNotification> {
    return Single.just(INSTANCE)
  }

  override fun clearFilteredCompetitionNames(): Single<StreamNotification> {
    return Single.just(INSTANCE)
  }

  override fun toggleFilteredTeamCode(teamCode: TeamCode): Single<StreamNotification> {
    return Single.just(INSTANCE)
  }

  override fun loadFilteredTeamCodes(): Single<List<TagName>> {
    return Single.just(emptyList())
  }

  override fun clearFilteredTeamCodes(): Single<StreamNotification> {
    return Single.just(INSTANCE)
  }

  override fun loadTeams(): Single<List<FilterTeam>> {
    return Single.just(emptyList())
  }

  override fun addTeam(filterTeam: FilterTeam): Single<StreamNotification> {
    return Single.just(INSTANCE)
  }
}