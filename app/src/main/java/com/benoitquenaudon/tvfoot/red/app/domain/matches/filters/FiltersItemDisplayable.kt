package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.support.annotation.StringRes
import com.benoitquenaudon.tvfoot.red.app.data.entity.Team

sealed class FiltersItemDisplayable {
  abstract fun isSameAs(other: FiltersItemDisplayable): Boolean

  sealed class FiltersAppliableItem : FiltersItemDisplayable() {
    data class FiltersCompetitionDisplayable(
        val code: String,
        val label: String,
        val filtered: Boolean
    ) : FiltersAppliableItem() {
      override fun isSameAs(other: FiltersItemDisplayable): Boolean {
        return if (other is FiltersCompetitionDisplayable) this.code == other.code else false
      }
    }

    data class FiltersTeamDisplayable(
        val code: String,
        val name: String,
        val type: String,
        val country: String,
        val filtered: Boolean
    ) : FiltersAppliableItem() {
      val logoPath: String
        get() = Team(
            code = code,
            type = type,
            country = country,
            id = null,
            name = null,
            url = null,
            city = null,
            fullname = null,
            stadium = null,
            twitter = null).logoPath

      override fun isSameAs(other: FiltersItemDisplayable): Boolean {
        return if (other is FiltersTeamDisplayable) {
          this.code == other.code
        } else {
          false
        }
      }
    }
  }

  object TeamSearchInputDisplayable : FiltersItemDisplayable() {
    override fun isSameAs(
        other: FiltersItemDisplayable) = other is TeamSearchInputDisplayable
  }

  data class TeamSearchResultDisplayable(
      val code: String,
      val name: String,
      val fullName: String,
      val type: String,
      val country: String
  ) : FiltersItemDisplayable() {
    val logoPath: String
      get() = Team(
          code = code,
          type = type,
          country = country,
          id = null,
          name = null,
          url = null,
          city = null,
          fullname = null,
          stadium = null,
          twitter = null).logoPath

    override fun isSameAs(other: FiltersItemDisplayable): Boolean {
      return if (other is TeamSearchResultDisplayable) {
        this.code == other.code
      } else {
        false
      }
    }

    companion object Factory {
      operator fun invoke(team: Team): TeamSearchResultDisplayable {
        // So, we need those but the server's API is funky so we check NPEs
        return TeamSearchResultDisplayable(
            code = checkNotNull(team.code) { "Team code is null" },
            name = checkNotNull(team.name) { "Team name is null" },
            fullName = checkNotNull(team.fullname) { "Team fullname is null" },
            type = checkNotNull(team.type) { "Team type is null" },
            country = checkNotNull(team.country) { "Country is null" }
        )
      }
    }
  }

  object FilterSearchLoadingRowDisplayable : FiltersItemDisplayable() {
    override fun isSameAs(other: FiltersItemDisplayable): Boolean {
      return other is FilterSearchLoadingRowDisplayable
    }
  }

  data class FilterHeaderDisplayable(
      @StringRes val headerStringId: Int
  ) : FiltersItemDisplayable() {
    override fun isSameAs(other: FiltersItemDisplayable): Boolean {
      return other is FilterHeaderDisplayable
    }
  }
}