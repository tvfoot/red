package com.benoitquenaudon.tvfoot.red.util

import java.text.Normalizer
import java.util.regex.Pattern

private val diacriticalPattern = Pattern.compile("[\\p{InCombiningDiacriticalMarks}]+")

fun String.stripAccents(): String {
  return diacriticalPattern
      .matcher(Normalizer.normalize(this, Normalizer.Form.NFD))
      .replaceAll("")
}