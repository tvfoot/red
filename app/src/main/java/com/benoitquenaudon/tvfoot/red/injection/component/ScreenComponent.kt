package com.benoitquenaudon.tvfoot.red.injection.component

import com.benoitquenaudon.tvfoot.red.injection.module.ActivityModule
import com.benoitquenaudon.tvfoot.red.injection.scope.ScreenScope
import dagger.Subcomponent

@ScreenScope @Subcomponent interface ScreenComponent {
  fun plus(activityModule: ActivityModule): ActivityComponent
}
