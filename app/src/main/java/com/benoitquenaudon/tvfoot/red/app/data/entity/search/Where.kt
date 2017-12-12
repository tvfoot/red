package com.benoitquenaudon.tvfoot.red.app.data.entity.search

import com.squareup.moshi.formatIso8601
import java.util.Date

data class Where(
    private val startAt: Long = System.currentTimeMillis(),
    private val deleted: Deleted = Deleted()
) {
  override fun toString() =
      """{"start-at":{"gte":"${Date(startAt).formatIso8601()}"},"deleted":$deleted}"""
}
