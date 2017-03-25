package io.oldering.tvfoot.red.flowcontroller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import javax.inject.Inject;
import javax.inject.Named;

import static io.oldering.tvfoot.red.util.Preconditions.checkNotNull;

public class FlowController {
  private final Context context;

  @Inject public FlowController(@Named("activity") Context context) {
    this.context = context;
  }

  public void toMatches() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tvfoot/"));
    context.startActivity(intent);
  }

  public void toMatch(String matchId) {
    checkNotNull(matchId, "matchId == null");

    // We don't need to set the in between 'league', 'home' and 'away' strings.
    Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse(String.format("https://tvfoot/match/league/home/away/%s", matchId)));
    context.startActivity(intent);
  }
}
