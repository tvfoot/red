package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

object LoadingRowDisplayable : MatchesItemDisplayable {
  override fun isSameAs(other: MatchesItemDisplayable): Boolean {
    return other == LoadingRowDisplayable
  }
}
