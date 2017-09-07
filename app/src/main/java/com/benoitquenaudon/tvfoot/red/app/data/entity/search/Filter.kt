package com.benoitquenaudon.tvfoot.red.app.data.entity.search

// filter:{"where":{"deleted":{"neq":1}},"order":"start-at ASC, weight ASC","limit":30}
data class Filter(
    val where: Where = Where(),
    val order: String = "start-at ASC, weight ASC",
    val limit: Int,
    val offset: Int
) {

  override fun toString() = """{"where":$where,"order":"$order","limit":$limit,"offset":$offset}"""
}
