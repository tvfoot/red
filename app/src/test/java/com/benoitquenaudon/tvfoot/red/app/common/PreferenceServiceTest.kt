package com.benoitquenaudon.tvfoot.red.app.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.benoitquenaudon.tvfoot.red.BuildConfig
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.properties.Delegates

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class PreferenceServiceTest {
  private val SHARED_PREF = "ref_test"
  private var matchId by Delegates.notNull<String>()
  private var preferenceService by Delegates.notNull<PreferenceService>()

  @Before fun before() {
    matchId = "matchid"
    preferenceService = PreferenceService(fakeSharedPreferences())
  }

  @Test fun loadNotifyMatchStart_shouldReturnDefaultValueWhenNone() {
    val obs = preferenceService.loadNotifyMatchStart(matchId).test()
    obs.assertValueCount(1)
    obs.assertComplete()
    obs.assertValue(false)
  }

  @Test fun loadNotifyMatchStart_shouldReturnTrue() {
    preferenceService.saveNotifyMatchStart(matchId, true)

    val obs = preferenceService.loadNotifyMatchStart(matchId).test()
    obs.assertValueCount(1)
    obs.assertComplete()
    obs.assertValue(true)
  }

  @Test fun loadNotifyMatchStart_shouldReturnFalse() {
    preferenceService.saveNotifyMatchStart(matchId, false)

    val obs = preferenceService.loadNotifyMatchStart(matchId).test()
    obs.assertValueCount(1)
    obs.assertComplete()
    obs.assertValue(false)
  }

  // is that enough?
  @Test fun saveNotifyMatchStart() {
    val obs = preferenceService.saveNotifyMatchStart(matchId, true).test()
    obs.assertValueCount(1)
    obs.assertComplete()
    obs.assertValue(StreamNotification.INSTANCE)
  }

  @SuppressLint("CommitPrefEdits") private fun fakeSharedPreferences(): SharedPreferences {
    val fakeSharedPreferences = RuntimeEnvironment.application.getSharedPreferences(SHARED_PREF,
        Context.MODE_PRIVATE)
    fakeSharedPreferences.edit().clear().commit()
    return fakeSharedPreferences
  }
}