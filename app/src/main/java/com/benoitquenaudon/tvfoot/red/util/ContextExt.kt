package com.benoitquenaudon.tvfoot.red.util

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat

fun Context.getDrawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)!!