package com.benoitquenaudon.tvfoot.red.app.common;

import android.os.Looper;
import javax.annotation.Nullable;
import io.reactivex.Observer;

public final class PreConditions {
  public static boolean checkMainThread(Observer<?> observer) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      observer.onError(new IllegalStateException(
          "Expected to be called on the main thread but was " + Thread.currentThread().getName()));
      return false;
    }
    return true;
  }

  public static <T> T checkNotNull(@Nullable T value, String message) {
    if (value == null) {
      throw new NullPointerException(message);
    }

    return value;
  }

  private PreConditions() {
    throw new AssertionError("No instances.");
  }
}
