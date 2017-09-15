package com.benoitquenaudon.tvfoot.red.injection.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchViewModel
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesViewModel
import com.benoitquenaudon.tvfoot.red.app.mvi.RedViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module abstract class ViewModelModule {
  @Binds
  @IntoMap
  @ViewModelKey(MatchesViewModel::class)
  internal abstract fun bindMatchesViewModel(matchesViewModel: MatchesViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(MatchViewModel::class)
  internal abstract fun bindMatchViewModel(matchViewModel: MatchViewModel): ViewModel

  @Binds internal abstract fun bindViewModelFactory(
      factory: RedViewModelFactory
  ): ViewModelProvider.Factory
}
