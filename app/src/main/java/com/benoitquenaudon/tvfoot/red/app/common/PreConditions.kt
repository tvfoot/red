package com.benoitquenaudon.tvfoot.red.app.common

import android.os.Looper
import io.reactivex.Observer

object PreConditions {
  @JvmStatic fun checkMainThread(observer: Observer<*>): Boolean {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      observer.onError(IllegalStateException(
          "Expected to be called on the main thread but was " + Thread.currentThread().name))
      return false
    }
    return true
  }

  @JvmStatic fun <T> checkNotNull(value: T,
      message: String): T = value ?: throw NullPointerException(message)
}
