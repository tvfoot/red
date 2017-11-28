package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

interface MatchesItemDisplayable {
  fun isSameAs(other: MatchesItemDisplayable): Boolean
}
