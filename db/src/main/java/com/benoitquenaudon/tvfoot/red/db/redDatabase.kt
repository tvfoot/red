@file:JvmName("RedDatabaseUtil")
package com.benoitquenaudon.tvfoot.red.db

import com.squareup.sqldelight.db.SqlDriver

/**
 * Wraps this database into a [RedDatabase]
 */
fun SqlDriver.createRedDatabase(): RedDatabase {
  return RedDatabase(
      driver = this,
      matchAdapter = TODO()
  )
}
