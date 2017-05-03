package io.oldering.tvfoot.red.app.common.flowcontroller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import javax.inject.Inject;

import static android.content.Intent.URI_INTENT_SCHEME;
import static io.oldering.tvfoot.red.app.common.PreConditions.checkNotNull;

public class FlowController {
  private final FirebaseAnalytics firebaseAnalytics;
  private final Activity activity;

  @Inject FlowController(FirebaseAnalytics firebaseAnalytics, Activity activity) {
    this.firebaseAnalytics = firebaseAnalytics;
    this.activity = activity;
  }

  public void toMatches() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tvfoot://tvfoot/"));
    navigate(intent);
  }

  public void toMatch(String matchId) {
    checkNotNull(matchId, "matchId == null");

    // We don't need to set the in between 'league', 'home' and 'away' strings.
    Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse(String.format("tvfoot://tvfoot/match/league/home/away/%s", matchId)));
    navigate(intent);
  }

  public void toSettings() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tvfoot://tvfoot/settings"));
    navigate(intent);
  }

  private void navigate(Intent intent) {
    logNavigation(intent);
    activity.startActivity(intent);
  }

  private void logNavigation(Intent intent) {
    Bundle params = new Bundle();
    params.putString("intent", intent.toUri(URI_INTENT_SCHEME));
    firebaseAnalytics.logEvent("navigation", params);
  }
}
