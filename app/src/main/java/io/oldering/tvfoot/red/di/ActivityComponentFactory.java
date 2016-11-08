package io.oldering.tvfoot.red.di;

import android.app.Activity;

import io.oldering.tvfoot.red.RedApp;

public final class ActivityComponentFactory {
    public static ActivityComponent create(Activity activity) {
        return ((RedApp) activity.getApplicationContext())
                .getComponent()
                .plusActivityComponent(new ActivityModule(activity));
    }
}
