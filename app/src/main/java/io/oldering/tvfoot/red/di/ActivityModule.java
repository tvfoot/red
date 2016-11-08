package io.oldering.tvfoot.red.di;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.util.SnackBarUtil;

@Module
public class ActivityModule {
    Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ScopeActivity
    SnackBarUtil provideSnackBarUtilBundleService() {
        return new SnackBarUtil(activity);
    }
}
