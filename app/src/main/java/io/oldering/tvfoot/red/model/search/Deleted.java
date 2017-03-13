package io.oldering.tvfoot.red.model.search;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class Deleted {
  static Deleted create() {
    return new AutoValue_Deleted(1);
  }

  // 0 or 1
  abstract int getNeq();

  @Override public String toString() {
    return "{" + "\"neq\":" + getNeq() + "}";
  }
}
