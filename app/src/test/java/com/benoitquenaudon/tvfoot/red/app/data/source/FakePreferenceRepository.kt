package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification.INSTANCE
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
}