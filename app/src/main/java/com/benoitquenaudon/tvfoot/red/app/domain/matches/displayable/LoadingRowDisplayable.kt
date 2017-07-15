package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

object LoadingRowDisplayable : MatchesItemDisplayable {
  override fun isSameAs(newItem: MatchesItemDisplayable): Boolean {
    return newItem is LoadingRowDisplayable
  }
}
