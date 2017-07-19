package com.benoitquenaudon.tvfoot.red.app.common.firebase

import android.os.Bundle
import android.support.annotation.NonNull
import android.support.annotation.Size

interface BaseRedFirebaseAnalytics {
  fun logEvent(@NonNull @Size(min = 1L, max = 40L) string: String, bundle: Bundle)
}