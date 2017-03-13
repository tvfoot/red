package io.oldering.tvfoot.red.flowcontroller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class FlowController {
  public static final String MATCH_VIEW_MODEL = "MATCH_VIEW_MODEL";

  public static void toMatchs(Context context) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tvfoot://matchsActivity"));
    context.startActivity(intent);
  }
}
