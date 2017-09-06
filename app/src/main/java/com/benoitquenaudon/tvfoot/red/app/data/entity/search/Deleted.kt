package com.benoitquenaudon.tvfoot.red.app.data.entity.search

data class Deleted(private val neq: Int = 1) {
  override fun toString() = """{"neq":$neq}"""
}
