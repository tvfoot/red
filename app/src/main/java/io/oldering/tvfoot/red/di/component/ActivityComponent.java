package io.oldering.tvfoot.red.di.component;

import dagger.Subcomponent;
import io.oldering.tvfoot.red.di.ActivityScope;
import io.oldering.tvfoot.red.di.module.ActivityModule;
import io.oldering.tvfoot.red.di.module.BundleModule;
import io.oldering.tvfoot.red.matches.MatchesActivity;

@ActivityScope @Subcomponent(modules = {
    ActivityModule.class, //
    BundleModule.class,
}) public interface ActivityComponent {
  //void inject(MatchDetailActivity matchDetailActivity);

  void inject(MatchesActivity matchesActivity);
}
