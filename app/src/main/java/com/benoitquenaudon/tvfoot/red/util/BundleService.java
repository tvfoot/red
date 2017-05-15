package com.benoitquenaudon.tvfoot.red.util;

import android.os.Bundle;
import javax.annotation.Nullable;

public class BundleService {
  private final Bundle data;
  public Bundle savedState;

  public BundleService(@Nullable Bundle savedState, @Nullable Bundle intentExtras) {
    data = new Bundle();

    this.savedState = savedState;
    if (this.savedState != null) {
      data.putAll(savedState);
    }
    if (intentExtras != null) {
      data.putAll(intentExtras);
    }
  }

  public Object get(String key) {
    return data.get(key);
  }

  public boolean contains(String key) {
    return data.containsKey(key);
  }

  public Bundle getAll() {
    return data;
  }
}
