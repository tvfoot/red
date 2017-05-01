package io.oldering.tvfoot.red.app.common.flowcontroller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import javax.inject.Inject;
import timber.log.Timber;

import static io.oldering.tvfoot.red.app.common.PreConditions.checkNotNull;

public class FlowController {
  private final Activity activity;

  @Inject FlowController(Activity activity) {
    this.activity = activity;
  }

  public void toMatches() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tvfoot://tvfoot/"));
    activity.startActivity(intent);
  }

  public void toMatch(String matchId) {
    checkNotNull(matchId, "matchId == null");

    // We don't need to set the in between 'league', 'home' and 'away' strings.
    Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse(String.format("tvfoot://tvfoot/match/league/home/away/%s", matchId)));
    activity.startActivity(intent);
  }

  public void toSettings() {
    // TODO(benoit)
    Timber.d("To Settings");
  }
}
