package com.benoitquenaudon.tvfoot.red.app.data.entity

import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag.TagType.BROADCASTER
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag.TagType.COMPETITION
import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Tag(
    val name: String,
    val desc: String,
    val type: TagType,
    val display: Boolean,
    val targets: List<String>
) {
  val isCompetition: Boolean = type == COMPETITION
  val isBroadcast: Boolean = type == BROADCASTER

  enum class TagType {
    @Json(name = "competition")
    COMPETITION,
    @Json(name = "broadcaster")
    BROADCASTER
  }
}