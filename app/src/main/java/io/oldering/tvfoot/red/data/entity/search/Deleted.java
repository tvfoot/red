package io.oldering.tvfoot.red.data.entity.search;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class Deleted {
  public static Builder builder() {
    return new AutoValue_Deleted.Builder().setNeq(1);
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder setNeq(int neq);

    public abstract Deleted build();
  }

  // 0 or 1
  public abstract int neq();

  @Override public String toString() {
    return "{" + "\"neq\":" + neq() + "}";
  }
}
