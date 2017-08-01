package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.content.Intent
import android.net.Uri
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchActivity
import org.junit.Rule
import org.junit.runner.RunWith

@LargeTest @RunWith(AndroidJUnit4::class) class MatchActivityTest {

  @Rule var activityTestRule: ActivityTestRule<MatchActivity> = object : ActivityTestRule<MatchActivity>(
      MatchActivity::class.java) {
    override fun getActivityIntent(): Intent {
      return Intent(Intent.ACTION_VIEW,
          Uri.parse("https://tvfoot/match/league/home/away/1"))
    }
  }
}
