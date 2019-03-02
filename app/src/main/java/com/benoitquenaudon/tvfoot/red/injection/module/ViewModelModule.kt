package com.benoitquenaudon.tvfoot.red.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
  abstract fun bindMatchesViewModel(matchesViewModel: MatchesViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(MatchViewModel::class)
  abstract fun bindMatchViewModel(matchViewModel: MatchViewModel): ViewModel

  @Binds abstract fun bindViewModelFactory(
      factory: RedViewModelFactory
  ): ViewModelProvider.Factory
}
