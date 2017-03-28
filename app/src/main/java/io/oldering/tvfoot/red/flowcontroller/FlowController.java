package io.oldering.tvfoot.red.flowcontroller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.TextView;
import io.oldering.tvfoot.red.match.MatchActivity;
import io.oldering.tvfoot.red.util.plaid.ReflowText;
import javax.inject.Inject;

import static io.oldering.tvfoot.red.util.Preconditions.checkNotNull;

public class FlowController {
  private final Activity context;

  @Inject public FlowController(Activity context) {
    this.context = context;
  }

  public void toMatches() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tvfoot://tvfoot/"));
    context.startActivity(intent);
  }

  public void toMatch(String matchId, TextView headline) {
    checkNotNull(matchId, "matchId == null");

    //// We don't need to set the in between 'league', 'home' and 'away' strings.
    //Intent intent = new Intent(Intent.ACTION_VIEW,
    //    Uri.parse(String.format("tvfoot://tvfoot/match/league/home/away/%s", matchId)));
    Intent intent = new Intent(context, MatchActivity.class);
    intent.putExtra("MATCH_ID", matchId);
    ReflowText.addExtras(intent, new ReflowText.ReflowableTextView(headline));
    ActivityOptionsCompat options =
        ActivityOptionsCompat.makeSceneTransitionAnimation(context, headline, "reflowing");
    context.startActivity(intent, options.toBundle());
  }
}
