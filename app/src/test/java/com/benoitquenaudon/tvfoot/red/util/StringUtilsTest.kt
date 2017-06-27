package com.benoitquenaudon.tvfoot.red.util

import org.junit.Test

import org.junit.Assert.assertEquals

class StringUtilsTest {
  @Test fun capitalize() {
    assertEquals("Foo", StringUtils.capitalize("foo"))
    assertEquals("111", StringUtils.capitalize("111"))
    assertEquals("", StringUtils.capitalize(""))
    assertEquals(null, StringUtils.capitalize(null))
    assertEquals("日本語", StringUtils.capitalize("日本語"))
  }
}
