package com.benoitquenaudon.tvfoot.red.injection.component

import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersFragment
import com.benoitquenaudon.tvfoot.red.injection.module.FragmentModule
import com.benoitquenaudon.tvfoot.red.injection.scope.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = arrayOf(
    FragmentModule::class))
interface FragmentComponent {
  fun inject(filtersFragment: FiltersFragment)
}