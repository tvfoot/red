package io.oldering.tvfoot.red.flowcontroller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import io.oldering.tvfoot.red.di.ActivityScope;
import javax.inject.Inject;
import javax.inject.Named;

import static io.oldering.tvfoot.red.util.Preconditions.checkNotNull;

@ActivityScope public class FlowController {
  private final Context context;

  @Inject public FlowController(@Named("activity") Context context) {
    this.context = context;
  }

  public void toMatches() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tvfoot/matches"));
    context.startActivity(intent);
  }

  public void toMatch(String matchId) {
    checkNotNull(matchId, "matchId == null");

    Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse(String.format("https://tvfoot/match/%s", matchId)));
    context.startActivity(intent);
  }
}
