package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

class FiltersCompetitionDisplayable(
    val code: String,
    val label: String,
    val filtered: Boolean
) : FiltersItemDisplayable {
  override fun isSameAs(other: FiltersItemDisplayable): Boolean {
    return if (other is FiltersCompetitionDisplayable) this.code == other.code else false
  }
}