package com.benoitquenaudon.tvfoot.red.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatter(template: String): Lazy<SimpleDateFormat> {
  return lazy {
    val formatter = SimpleDateFormat(template, Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    formatter
  }
}