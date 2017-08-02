package com.benoitquenaudon.tvfoot.red.app.data.entity

data class Team(
    val code: String?,
    val name: String?,
    val fullName: String?,
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
}
