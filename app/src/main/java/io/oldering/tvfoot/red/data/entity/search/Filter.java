package io.oldering.tvfoot.red.data.entity.search;

import com.google.auto.value.AutoValue;

// filter:{"where":{"deleted":{"neq":1}},"order":"start-at ASC, weight ASC","limit":30}
@AutoValue public abstract class Filter {
  public static Builder builder() {
    return new AutoValue_Filter.Builder().order("start-at ASC, weight ASC")
        .where(Where.builder().build());
  }

  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder where(Where where);

    public abstract Builder order(String order);

    public abstract Builder limit(int limit);

    public abstract Builder offset(int offset);

    public abstract Filter build();
  }

  public abstract Where where();

  public abstract String order();

  public abstract int limit();

  public abstract int offset();

  @Override public String toString() {
    return "{"
        + "\"where\":"
        + where()
        + ","
        + "\"order\":\""
        + order()
        + "\","
        + "\"limit\":"
        + limit()
        + ","
        + "\"offset\":"
        + offset()
        + "}";
  }
}
