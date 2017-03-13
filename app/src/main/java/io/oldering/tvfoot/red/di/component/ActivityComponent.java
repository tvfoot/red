package io.oldering.tvfoot.red.di.component;

import dagger.Subcomponent;
import io.oldering.tvfoot.red.di.ScopeActivity;
import io.oldering.tvfoot.red.di.module.ActivityModule;
import io.oldering.tvfoot.red.di.module.BundleModule;

@ScopeActivity @Subcomponent(modules = {
    ActivityModule.class, //
    BundleModule.class,
}) public interface ActivityComponent {
  //void inject(MatchListActivity matchListActivity);
  //
  //void inject(MatchDetailActivity matchDetailActivity);
}
