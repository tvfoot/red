package io.oldering.tvfoot.red.di.component;

import dagger.Subcomponent;
import io.oldering.tvfoot.red.di.module.ActivityModule;
import io.oldering.tvfoot.red.di.scope.ScreenScope;

@ScreenScope @Subcomponent public interface ScreenComponent {
  ActivityComponent plus(ActivityModule activityModule);
}
