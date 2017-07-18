package com.benoitquenaudon.tvfoot.red.app.domain.matches;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.benoitquenaudon.tvfoot.red.util.BasicActivityTest;
import org.junit.Rule;
import org.junit.runner.RunWith;

@LargeTest @RunWith(AndroidJUnit4.class) public class MatchesActivityTest
    extends BasicActivityTest {

  @Rule public ActivityTestRule<MatchesActivity> activityTestRule =
      new ActivityTestRule<>(MatchesActivity.class);
}
