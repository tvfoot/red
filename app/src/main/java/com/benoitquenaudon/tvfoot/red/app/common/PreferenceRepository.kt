package com.benoitquenaudon.tvfoot.red.app.common

import android.content.SharedPreferences
import io.reactivex.Single
import javax.inject.Inject

class PreferenceRepository @Inject constructor(private val preferences: SharedPreferences) {

  fun saveNotifyMatchStart(matchId: String,
      notifyMatchStart: Boolean): Single<StreamNotification> {
    preferences.edit().putBoolean(notifyMatchStartPrefKey(matchId), notifyMatchStart).apply()
    return Single.just(StreamNotification.INSTANCE)
  }

  fun loadNotifyMatchStart(matchId: String): Single<Boolean> {
    return Single.just(preferences.getBoolean(notifyMatchStartPrefKey(matchId), false))
  }

  private fun notifyMatchStartPrefKey(matchId: String): String {
    return String.format("NOTIFY_MATCH_START_%s", matchId)
  }
}