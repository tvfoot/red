package com.benoitquenaudon.tvfoot.red.app.data.entity.search;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class Deleted {
  public static Builder builder() {
    return new AutoValue_Deleted.Builder().neq(1);
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder neq(int neq);

    public abstract Deleted build();
  }

  // 0 or 1
  public abstract int neq();

  @Override public String toString() {
    return "{" + "\"neq\":" + neq() + "}";
  }
}
