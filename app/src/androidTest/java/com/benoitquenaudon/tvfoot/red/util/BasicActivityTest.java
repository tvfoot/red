package com.benoitquenaudon.tvfoot.red.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static org.hamcrest.Matchers.anyOf;

public class BasicActivityTest {
  protected Context targetContext() {
    return InstrumentationRegistry.getTargetContext();
  }

  protected void navigateUp() {
    onView(anyOf(withContentDescription("Navigation up"), withContentDescription("上へ移動"))).check(
        matches(isDisplayed())).perform(click());
  }
}
