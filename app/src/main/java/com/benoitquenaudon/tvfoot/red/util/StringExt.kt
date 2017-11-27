package com.benoitquenaudon.tvfoot.red.util

import java.text.Normalizer
import java.util.regex.Pattern

private val DIACRITICS_AND_FRIENDS =
    Pattern.compile("[\\p{InCombiningDiacriticalMarks}]+")

fun String.stripAccents(): String {
  return DIACRITICS_AND_FRIENDS
      .matcher(Normalizer.normalize(this, Normalizer.Form.NFD))
      .replaceAll("")
}