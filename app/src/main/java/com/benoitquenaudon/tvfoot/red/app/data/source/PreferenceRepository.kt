package com.benoitquenaudon.tvfoot.red.app.data.source

import android.content.SharedPreferences
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification.INSTANCE
import io.reactivex.Single
import javax.inject.Inject

class PreferenceRepository @Inject constructor(
    private val preferences: SharedPreferences
) : BasePreferenceRepository {

  override fun saveNotifyMatchStart(matchId: String,
      notifyMatchStart: Boolean): Single<StreamNotification> {
    preferences.edit().putBoolean(notifyMatchStartPrefKey(matchId), notifyMatchStart).apply()
    return Single.just(INSTANCE)
  }

  override fun loadNotifyMatchStart(matchId: String): Single<Boolean> {
    return Single.just(preferences.getBoolean(notifyMatchStartPrefKey(matchId), false))
  }

  private fun notifyMatchStartPrefKey(matchId: String): String {
    return String.format("NOTIFY_MATCH_START_%s", matchId)
  }
}