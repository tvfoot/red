package com.benoitquenaudon.tvfoot.red.app.data.source

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification.INSTANCE
import com.benoitquenaudon.tvfoot.red.util.MatchId
import com.benoitquenaudon.tvfoot.red.util.TagName
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

    private fun notifyMatchStartPrefKey(matchId: String): String {
      return String.format("NOTIFY_MATCH_START_%s", matchId)
    }
  }
}