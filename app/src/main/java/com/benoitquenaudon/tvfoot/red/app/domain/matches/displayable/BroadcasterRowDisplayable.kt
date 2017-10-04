package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BroadcasterRowDisplayable(
    val name: String,
    private val broadcasterCode: String
) : Parcelable {
  val logoPath: String
    get() = "/images/broadcasters/${broadcasterCode}_120.png"
}
