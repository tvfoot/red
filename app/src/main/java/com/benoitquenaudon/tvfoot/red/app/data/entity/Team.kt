package com.benoitquenaudon.tvfoot.red.app.data.entity

import com.benoitquenaudon.tvfoot.red.util.stripAccents

data class Team(
    val id: String?,
    val code: String?,
    val name: String?,
    val fullname: String?,
    val city: String?,
    val country: String?,
    val url: String?,
    val stadium: String?,
    val twitter: String?,
    val type: String?
) {
  val isEmpty: Boolean
    get() = name == null

  companion object Constant {
    val DEFAULT_CODE = "default"
  }

  val logoPath: String
    get() = if (code == null || type == null) {
      "/images/teams/default/large/default.png"
    } else {
      when (type) {
        "nation" -> String.format("/images/teams/nations/large/%s.png", code.toLowerCase())
        "club" -> {
          val country = checkNotNull(country) { "team's country should not be null" }
          "/images/teams/${country.stripAccents()}/large/${code.toLowerCase()}.png"
        }
        else -> throw IllegalStateException("Unknown type " + type)
      }
    }
}
