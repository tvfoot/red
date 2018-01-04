package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification
import com.benoitquenaudon.tvfoot.red.util.TagName
import io.reactivex.Single

interface BasePreferenceRepository {
  fun saveNotifyMatchStart(matchId: String, notifyMatchStart: Boolean): Single<StreamNotification>
  fun loadNotifyMatchStart(matchId: String): Single<Boolean>
  fun toggleFilteredBroadcasterName(broadcasterName: TagName): Single<StreamNotification>
  fun toggleFilteredCompetitionName(competitionName: TagName): Single<StreamNotification>
  fun loadFilteredBroadcasterNames(): Single<List<TagName>>
  fun loadFilteredCompetitionNames(): Single<List<TagName>>
  fun clearFilteredCompetitionNames(): Single<StreamNotification>
  fun clearFilteredBroadcasterNames(): Single<StreamNotification>
}