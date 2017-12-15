package com.squareup.moshi

import java.util.Date

/**
 * I should, of course, never do that.
 */
internal fun String.parseIso8601() = Iso8601Utils.parse(this)

internal fun Date.formatIso8601() = Iso8601Utils.format(this)