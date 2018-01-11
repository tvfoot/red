package com.benoitquenaudon.tvfoot.red.injection.module

import android.app.Activity
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesActivity
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersFragment
import com.benoitquenaudon.tvfoot.red.injection.scope.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MatchesActivityModule {
  @Binds abstract fun provideActivity(activity: MatchesActivity): Activity

  @FragmentScope
  @ContributesAndroidInjector
  internal abstract fun filtersFragment(): FiltersFragment
}