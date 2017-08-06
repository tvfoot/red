package com.benoitquenaudon.tvfoot.red.app.common.flowcontroller

import android.content.Intent
import android.net.Uri
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FlowIntentFactoryTest {
  @Test fun toMatches() {
    val intent = FlowIntentFactory.toMatchesIntent()
    Assert.assertEquals(Intent.ACTION_VIEW, intent.action)
    Assert.assertEquals(Uri.parse("tvfoot://tvfoot/"), intent.data)
  }

  @Test fun toMatch() {
    val matchId = "match_id"
    val intent = FlowIntentFactory.toMatchIntent(matchId)
    Assert.assertEquals(Intent.ACTION_VIEW, intent.action)
    Assert.assertEquals(Uri.parse("tvfoot://tvfoot/match/league/home/away/$matchId"), intent.data)
  }

  @Test fun toSettings() {
    val intent = FlowIntentFactory.toSettingsIntent()
    Assert.assertEquals(Intent.ACTION_VIEW, intent.action)
    Assert.assertEquals(Uri.parse("tvfoot://tvfoot/settings"), intent.data)
  }
}