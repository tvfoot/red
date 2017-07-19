package com.benoitquenaudon.tvfoot.red.app.domain.matches;

import android.content.Intent;
import android.net.Uri;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchActivity;
import com.benoitquenaudon.tvfoot.red.util.BasicActivityTest;
import org.junit.Rule;
import org.junit.runner.RunWith;

@LargeTest @RunWith(AndroidJUnit4.class) public class MatchActivityTest extends BasicActivityTest {

  @Rule public ActivityTestRule<MatchActivity> activityTestRule =
      new ActivityTestRule<MatchActivity>(MatchActivity.class) {
        @Override protected Intent getActivityIntent() {
          return new Intent(Intent.ACTION_VIEW,
              Uri.parse("https://tvfoot/match/league/home/away/1"));
        }
      };
}
