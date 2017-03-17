package io.oldering.tvfoot.red.data.entity.search;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class Where {
  public static Builder builder() {
    return new AutoValue_Where.Builder().setDeleted(Deleted.builder().build());
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder setDeleted(Deleted deleted);

    public abstract Where build();
  }

  abstract Deleted deleted();

  @Override public String toString() {
    return "{" + "\"deleted\":" + deleted() + "}";
  }
}
