package com.benoitquenaudon.tvfoot.red.util

object PreConditions {
  @JvmStatic
  fun <T> checkNotNull(value: T,
      message: String): T = value ?: throw NullPointerException(message)
}
