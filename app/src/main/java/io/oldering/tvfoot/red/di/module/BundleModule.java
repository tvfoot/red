package io.oldering.tvfoot.red.di.module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.di.ScopeActivity;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import io.oldering.tvfoot.red.util.BundleService;
import io.oldering.tvfoot.red.view.BaseActivity;
import io.oldering.tvfoot.red.viewmodel.MatchViewModel;

@Module public class BundleModule {
  @Provides @ScopeActivity BundleService provideBundleService(BaseActivity activity) {
    return activity.getBundleService();
  }

  // TODO(benoit) not applicable to FlowController since it's static methods all over
  // is necessary when providing same type but different values
  // @AssetId
  @Provides @ScopeActivity MatchViewModel provideMatchViewModel(BundleService bundleService) {
    return (MatchViewModel) bundleService.get(FlowController.MATCH_VIEW_MODEL);
  }

  @Provides public Bundle provideBundle(Activity context) {
    return context.getIntent().getExtras() == null ? new Bundle() : context.getIntent().getExtras();
  }

  @Provides public Intent provideIntent(Activity context) {
    return context.getIntent() == null ? new Intent() : context.getIntent();
  }
}
