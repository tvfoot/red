package io.oldering.tvfoot.red.di.component;

import dagger.Subcomponent;
import io.oldering.tvfoot.red.di.module.ActivityModule;
import io.oldering.tvfoot.red.di.module.BundleModule;
import io.oldering.tvfoot.red.di.scope.ActivityScope;
import io.oldering.tvfoot.red.main.MainActivity;
import io.oldering.tvfoot.red.match.MatchActivity;
import io.oldering.tvfoot.red.matches.MatchesActivity;

@ActivityScope @Subcomponent(modules = {
    ActivityModule.class, //
    BundleModule.class,
}) public interface ActivityComponent {
  void inject(MainActivity mainActivity);

  void inject(MatchesActivity matchesActivity);

  void inject(MatchActivity matchActivity);
}