package io.oldering.tvfoot.red.app.domain.matches;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.jakewharton.espresso.OkHttp3IdlingResource;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.RedApp;
import io.oldering.tvfoot.red.util.BasicActivityTest;
import okhttp3.OkHttpClient;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest @RunWith(AndroidJUnit4.class) public class MatchesActivityTest
    extends BasicActivityTest {

  @Rule public ActivityTestRule<MatchesActivity> activityTestRule =
      new ActivityTestRule<>(MatchesActivity.class);

  @Ignore("not doind so well...") @Test public void loadFirstPage() {
    RedApp app = (RedApp) activityTestRule.getActivity().getApplication();
    OkHttpClient client = app.getComponent().okHttpClient();
    Espresso.registerIdlingResources(OkHttp3IdlingResource.create("OkHttp", client));

    onView(withId(R.id.loading_view)).check(matches(isDisplayed()));

    onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
        .perform(scrollTo(withId(R.id.progress_paging)));

    onView(withId(R.id.progress_paging)).check(matches(isDisplayed()));
  }
}
