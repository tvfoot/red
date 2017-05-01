package io.oldering.tvfoot.red.app.injection.component;

import dagger.Subcomponent;
import io.oldering.tvfoot.red.app.domain.main.MainActivity;
import io.oldering.tvfoot.red.app.domain.match.MatchActivity;
import io.oldering.tvfoot.red.app.domain.matches.MatchesActivity;
import io.oldering.tvfoot.red.app.injection.module.ActivityModule;
import io.oldering.tvfoot.red.app.injection.module.BundleModule;
import io.oldering.tvfoot.red.app.injection.scope.ActivityScope;

@ActivityScope @Subcomponent(modules = {
    ActivityModule.class, //
    BundleModule.class,
}) public interface ActivityComponent {
  void inject(MainActivity mainActivity);

  void inject(MatchesActivity matchesActivity);

  void inject(MatchActivity matchActivity);
}