package com.benoitquenaudon.tvfoot.red.db.debug

import com.benoitquenaudon.tvfoot.red.db.debug.DebuggableSqlPreparedStatement
import com.squareup.sqldelight.db.SqlPreparedStatement

class SqlBinderWrapper(
  private val binders: ((SqlPreparedStatement) -> Unit)?
) : (SqlPreparedStatement) -> Unit {

  var boundParameters: Map<Int, Any?>? = null
    private set

  override fun invoke(statement: SqlPreparedStatement) {
    val debuggableStatement = DebuggableSqlPreparedStatement(statement)
    binders?.invoke(debuggableStatement)
    boundParameters = debuggableStatement.boundParameters
  }
}