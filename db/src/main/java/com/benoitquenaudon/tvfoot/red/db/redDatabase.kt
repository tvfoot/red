@file:JvmName("RedDatabaseUtil")
package com.benoitquenaudon.tvfoot.red.db

import com.benoitquenaudon.tvfoot.red.db2.Match
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver

/**
 * Wraps this database into a [RedDatabase]
 */
fun SqlDriver.createRedDatabase(): RedDatabase {
  return RedDatabase(
      driver = this,
      matchAdapter = Match.Adapter(
          broadcasterCodesAdapter = BroadcasterAdapter,
          tagsAdapter = TagsAdapter
      )
  )
}

object BroadcasterAdapter : ColumnAdapter<List<String>, String> {
  override fun decode(databaseValue: String): List<String> {
    return databaseValue.split(",")
  }

  override fun encode(value: List<String>): String {
    return value.joinToString(",")
  }
}

object TagsAdapter : ColumnAdapter<List<String>, String> {
  override fun decode(databaseValue: String): List<String> {
    return databaseValue.split(",")
  }

  override fun encode(value: List<String>): String {
    return value.joinToString(",")
  }
}