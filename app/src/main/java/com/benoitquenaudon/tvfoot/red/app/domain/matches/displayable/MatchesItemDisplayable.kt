package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

interface MatchesItemDisplayable {
  fun isSameAs(newItem: MatchesItemDisplayable): Boolean
}
