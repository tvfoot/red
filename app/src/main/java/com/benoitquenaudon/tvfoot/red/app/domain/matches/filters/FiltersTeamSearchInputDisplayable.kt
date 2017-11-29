package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

object FiltersTeamSearchInputDisplayable : FiltersItemDisplayable {
  override fun isSameAs(other: FiltersItemDisplayable) = other is FiltersTeamSearchInputDisplayable
}