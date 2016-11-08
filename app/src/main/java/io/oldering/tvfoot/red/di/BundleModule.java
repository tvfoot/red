package io.oldering.tvfoot.red.di;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import io.oldering.tvfoot.red.util.BundleService;
import io.oldering.tvfoot.red.view.BaseActivity;
import io.oldering.tvfoot.red.viewmodel.MatchViewModel;

@Module
public class BundleModule {
    @Provides
    @ScopeActivity
    BundleService provideBundleService(BaseActivity activity) {
        return activity.getBundleService();
    }

    // TODO(benoit) not applicable to FlowController since it's static methods all over
    // is necessary when providing same type but different values
    // @AssetId
    @Provides
    @ScopeActivity
    MatchViewModel provideMatchViewModel(BundleService bundleService) {
        return (MatchViewModel) bundleService.getParcelable(FlowController.MATCH_VIEW_MODEL);
    }
}
