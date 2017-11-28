package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

interface FiltersItemDisplayable {
  fun isSameAs(other: FiltersItemDisplayable): Boolean
}