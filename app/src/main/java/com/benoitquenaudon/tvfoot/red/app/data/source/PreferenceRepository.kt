package com.benoitquenaudon.tvfoot.red.app.data.source

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification.INSTANCE
import com.benoitquenaudon.tvfoot.red.util.MatchId
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

  public fun loadToBeNotifiedMatchIds(): Observable<MatchId> {
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
    private fun notifyMatchStartPrefKey(matchId: String): String {
      return String.format("NOTIFY_MATCH_START_%s", matchId)
    }
  }
}