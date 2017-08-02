package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith

@LargeTest @RunWith(AndroidJUnit4::class) class MatchesActivityTest() {

  @Rule var activityTestRule = ActivityTestRule(MatchesActivity::class.java)
}
