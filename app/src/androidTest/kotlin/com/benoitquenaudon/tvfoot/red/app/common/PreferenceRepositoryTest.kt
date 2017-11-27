package com.benoitquenaudon.tvfoot.red.app.common

import android.content.Context
import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.benoitquenaudon.tvfoot.red.app.data.source.PreferenceRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferenceRepositoryTest {
  companion object Constants {
    private val SHARED_PREF = "ref_test"
  }

  private lateinit var matchId: String
  private lateinit var preferenceService: PreferenceRepository

  @Before
  fun before() {
    matchId = "matchid"
    preferenceService = PreferenceRepository(
        fakeSharedPreferences())
  }

  @Test
  fun loadNotifyMatchStart_shouldReturnDefaultValueWhenNone() {
    val obs = preferenceService.loadNotifyMatchStart(matchId).test()
    obs.assertValueCount(1)
    obs.assertComplete()
    obs.assertValue(false)
  }

  @Test
  fun loadNotifyMatchStart_shouldReturnTrue() {
    preferenceService.saveNotifyMatchStart(matchId, true)

    val obs = preferenceService.loadNotifyMatchStart(matchId).test()
    obs.assertValueCount(1)
    obs.assertComplete()
    obs.assertValue(true)
  }

  @Test
  fun loadNotifyMatchStart_shouldReturnFalse() {
    preferenceService.saveNotifyMatchStart(matchId, false)

    val obs = preferenceService.loadNotifyMatchStart(matchId).test()
    obs.assertValueCount(1)
    obs.assertComplete()
    obs.assertValue(false)
  }

  @Test
  fun saveNotifyMatchStart() {
    val obs = preferenceService.saveNotifyMatchStart(matchId, true).test()
    obs.assertValueCount(1)
    obs.assertComplete()
    obs.assertValue(StreamNotification.INSTANCE)
  }

  private fun fakeSharedPreferences(): SharedPreferences =
      InstrumentationRegistry
          .getTargetContext()
          .applicationContext
          .getSharedPreferences(
              SHARED_PREF,
              Context.MODE_PRIVATE)
          .also { it.edit().clear().apply() }
}