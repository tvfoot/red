package io.oldering.tvfoot.red.util;

import android.os.Looper;
import io.reactivex.Observer;

public final class Preconditions {
  public static boolean checkMainThread(Observer<?> observer) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      observer.onError(new IllegalStateException(
          "Expected to be called on the main thread but was " + Thread.currentThread().getName()));
      return false;
    }
    return true;
  }

  private Preconditions() {
    throw new AssertionError("No instances.");
  }
}
