package com.benoitquenaudon.tvfoot.red.app.data.entity.search

data class TagsFilter(private val order: String = "weight ASC") {
  override fun toString() = """{"order":"$order"}"""
}