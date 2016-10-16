package io.oldering.tvfoot.red.flowcontroller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class FlowController {
    public static void launchMatchListActivity(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tvfoot://matchListActivity"));
        context.startActivity(intent);
    }
}
