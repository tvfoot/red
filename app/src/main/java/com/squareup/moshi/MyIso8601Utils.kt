package com.squareup.moshi

import java.util.Date

/**
 * bad boy
 */
internal fun String.parseIso8601() = Iso8601Utils.parse(this)

internal fun Date.formatIso8601() = Iso8601Utils.format(this)