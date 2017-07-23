package com.benoitquenaudon.tvfoot.red.app.injection.component

import com.benoitquenaudon.tvfoot.red.app.injection.module.ActivityModule
import com.benoitquenaudon.tvfoot.red.app.injection.scope.ScreenScope
import dagger.Subcomponent

@ScreenScope @Subcomponent interface ScreenComponent {
  fun plus(activityModule: ActivityModule): ActivityComponent
}
