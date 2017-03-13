package io.oldering.tvfoot.red.data.model.search;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class Where {
  static Where create() {
    return new AutoValue_Where(Deleted.create());
  }

  abstract Deleted getDeleted();

  @Override public String toString() {
    return "{" + "\"deleted\":" + getDeleted() + "}";
  }
}
