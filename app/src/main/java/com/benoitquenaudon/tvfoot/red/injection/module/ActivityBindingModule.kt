package com.benoitquenaudon.tvfoot.red.injection.module

import com.benoitquenaudon.tvfoot.red.app.domain.libraries.LibrariesActivity
import com.benoitquenaudon.tvfoot.red.app.domain.main.MainActivity
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchActivity
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesActivity
import com.benoitquenaudon.tvfoot.red.injection.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
  @ActivityScope
  @ContributesAndroidInjector(modules = [MainActivityModule::class])
  abstract fun contributeMainActivityInjector(): MainActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [MatchesActivityModule::class])
  abstract fun contributeMatchesActivityInjector(): MatchesActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [MatchActivityModule::class])
  abstract fun contributeMatchActivityInjector(): MatchActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [LibrariesActivityModule::class])
  abstract fun contributeLibrariesActivityInjector(): LibrariesActivity
}