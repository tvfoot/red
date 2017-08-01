package com.benoitquenaudon.tvfoot.red.app.data.entity.search

data class Deleted(val neq: Int = 1) {
  override fun toString() = """{"neq":$neq}"""
}
