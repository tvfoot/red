package com.benoitquenaudon.tvfoot.red.db.debug

import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.db.SqlPreparedStatement
import timber.log.Timber

class DebuggableSqlDriver(
  private val driver: SqlDriver
) : SqlDriver by driver {

  var enableLogging = false

  private fun <T> executeAndPrettyPrint(
    sql: String,
    binders: ((SqlPreparedStatement) -> Unit)?,
    execute: (SqlBinderWrapper) -> T
  ): T {
    val wrapped = SqlBinderWrapper(binders)
    val startTime = System.currentTimeMillis()
    return try {
      execute(wrapped)
    } finally {
      val took = System.currentTimeMillis() - startTime
      Timber.i(
          """
EXECUTED:
$sql

BOUND ARGUMENTS:
${wrapped.boundParameters}

TOOK:
${took}ms
""".trimIndent()
      )
    }
  }

  override fun execute(
    identifier: Int?,
    sql: String,
    parameters: Int,
    binders: (SqlPreparedStatement.() -> Unit)?
  ) {
    if (!enableLogging) {
      driver.execute(identifier, sql, parameters, binders)
    } else {
      executeAndPrettyPrint(sql, binders) {
        driver.execute(identifier, sql, parameters, it)
      }
    }
  }

  override fun executeQuery(
    identifier: Int?,
    sql: String,
    parameters: Int,
    binders: (SqlPreparedStatement.() -> Unit)?
  ): SqlCursor {
    return if (!enableLogging) {
      driver.executeQuery(identifier, sql, parameters, binders)
    } else {
      executeAndPrettyPrint(sql, binders) {
        driver.executeQuery(identifier, sql, parameters, it)
      }
    }
  }
}