package io.oldering.tvfoot.red.data.model.search;

import com.google.auto.value.AutoValue;

// filter:{"where":{"deleted":{"neq":1}},"order":"start-at ASC, weight ASC","limit":30}
@AutoValue public abstract class Filter {
  public static Filter create(int limit, int offset) {
    return new AutoValue_Filter(Where.create(), "start-at ASC, weight ASC", limit, offset);
  }

  abstract Where getWhere();

  abstract String getOrder();

  abstract int getLimit();

  abstract int getOffset();

  @Override public String toString() {
    return "{"
        + "\"where\":"
        + getWhere()
        + ","
        + "\"order\":\""
        + getOrder()
        + "\","
        + "\"limit\":"
        + getLimit()
        + ","
        + "\"offset\":"
        + getOffset()
        + "}";
  }
}
