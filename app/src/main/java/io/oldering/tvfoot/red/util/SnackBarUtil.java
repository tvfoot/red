package io.oldering.tvfoot.red.util;

import android.app.Activity;
import android.support.design.widget.Snackbar;

public class SnackBarUtil {
    private Activity activity;

    public SnackBarUtil(Activity activity) {
        this.activity = activity;
    }

    public Snackbar makeSnackBar(String text, int duration) {
        // TODO(benoit) check some real usecase to see what should be the passed view.
        return Snackbar.make(activity.getCurrentFocus(), text, duration);
    }
}
