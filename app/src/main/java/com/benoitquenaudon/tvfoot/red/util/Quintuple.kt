package com.benoitquenaudon.tvfoot.red.util

import java.io.Serializable

/**
 * Represents a quintuple of values
 *
 * There is no meaning attached to values in this class, it can be used for any purpose.
 * Quintuple exhibits value semantics, i.e. two quintuples are equal if all three components are equal.
 *
 * @param A type of the first value.
 * @param B type of the second value.
 * @param C type of the third value.
 * @param D type of the third value.
 * @property first First value.
 * @property second Second value.
 * @property third Third value.
 * @property fourth Third value.
 */
data class Quintuple<out A, out B, out C, out D, out E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
) : Serializable {

  /**
   * Returns string representation of the [Triple] including its [first], [second], [third],
   * [fourth] and [fifth] values.
   */
  override fun toString(): String = "($first, $second, $third, $fourth, $fifth)"
}
