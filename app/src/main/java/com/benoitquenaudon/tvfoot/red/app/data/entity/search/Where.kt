package com.benoitquenaudon.tvfoot.red.app.data.entity.search

data class Where(val deleted: Deleted = Deleted()) {
  override fun toString() = """{"deleted":$deleted}"""
}
