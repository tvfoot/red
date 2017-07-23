package com.benoitquenaudon.tvfoot.red.app.injection.component

import com.benoitquenaudon.tvfoot.red.app.domain.main.MainActivity
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchActivity
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesActivity
import com.benoitquenaudon.tvfoot.red.app.injection.module.ActivityModule
import com.benoitquenaudon.tvfoot.red.app.injection.scope.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(
    ActivityModule::class))
interface ActivityComponent {
  fun inject(mainActivity: MainActivity)

  fun inject(matchesActivity: MatchesActivity)

  fun inject(matchActivity: MatchActivity)
}