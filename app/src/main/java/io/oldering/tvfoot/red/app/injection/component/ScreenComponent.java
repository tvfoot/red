package io.oldering.tvfoot.red.app.injection.component;

import dagger.Subcomponent;
import io.oldering.tvfoot.red.app.injection.module.ActivityModule;
import io.oldering.tvfoot.red.app.injection.scope.ScreenScope;

@ScreenScope @Subcomponent public interface ScreenComponent {
  ActivityComponent plus(ActivityModule activityModule);
}
