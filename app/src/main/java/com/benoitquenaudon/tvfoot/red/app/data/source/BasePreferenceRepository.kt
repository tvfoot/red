package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification
import io.reactivex.Single

interface BasePreferenceRepository {
  // TODO(benoit) should return a Completable, no need for StreamNotification here
  fun saveNotifyMatchStart(matchId: String, notifyMatchStart: Boolean): Single<StreamNotification>
  fun loadNotifyMatchStart(matchId: String): Single<Boolean>
}