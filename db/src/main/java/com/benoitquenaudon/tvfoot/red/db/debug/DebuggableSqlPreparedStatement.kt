package com.benoitquenaudon.tvfoot.red.db.debug

import com.squareup.sqldelight.db.SqlPreparedStatement

class DebuggableSqlPreparedStatement(
  private val statement: SqlPreparedStatement
) : SqlPreparedStatement {

  val boundParameters = HashMap<Int, Any?>()

  override fun bindBytes(
    index: Int,
    value: ByteArray?
  ) {
    boundParameters[index] = value
    statement.bindBytes(index, value)
  }

  override fun bindDouble(
    index: Int,
    value: Double?
  ) {
    boundParameters[index] = value
    statement.bindDouble(index, value)
  }

  override fun bindLong(
    index: Int,
    value: Long?
  ) {
    boundParameters[index] = value
    statement.bindLong(index, value)
  }

  override fun bindString(
    index: Int,
    value: String?
  ) {
    boundParameters[index] = value
    statement.bindString(index, value)
  }
}