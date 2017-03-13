package io.oldering.tvfoot.red.di.module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.di.ScopeActivity;
import io.oldering.tvfoot.red.util.BundleService;
import io.oldering.tvfoot.red.view.BaseActivity;

@Module public class BundleModule {
  @Provides @ScopeActivity BundleService provideBundleService(BaseActivity activity) {
    return activity.getBundleService();
  }

  @Provides public Bundle provideBundle(Activity context) {
    return context.getIntent().getExtras() == null ? new Bundle() : context.getIntent().getExtras();
  }

  @Provides public Intent provideIntent(Activity context) {
    return context.getIntent() == null ? new Intent() : context.getIntent();
  }
}
