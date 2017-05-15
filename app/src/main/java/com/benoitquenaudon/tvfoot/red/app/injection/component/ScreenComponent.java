package com.benoitquenaudon.tvfoot.red.app.injection.component;

import dagger.Subcomponent;
import com.benoitquenaudon.tvfoot.red.app.injection.module.ActivityModule;
import com.benoitquenaudon.tvfoot.red.app.injection.scope.ScreenScope;

@ScreenScope @Subcomponent public interface ScreenComponent {
  ActivityComponent plus(ActivityModule activityModule);
}
