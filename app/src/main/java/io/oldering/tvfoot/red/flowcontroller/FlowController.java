package io.oldering.tvfoot.red.flowcontroller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import io.oldering.tvfoot.red.viewmodel.MatchViewModel;

public class FlowController {
    public static final String MATCH_VIEW_MODEL = "MATCH_VIEW_MODEL";

    public static void launchMatchListActivity(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tvfoot://matchListActivity"));
        context.startActivity(intent);
    }

    public static void launchMatchDetailActivity(Context context, MatchViewModel matchVM) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tvfoot://matchDetailActivity"));
        intent.putExtra(MATCH_VIEW_MODEL, matchVM);
        context.startActivity(intent);
    }
}
