package io.oldering.tvfoot.red.model.search;

import com.google.auto.value.AutoValue;

// filter:{"where":{"deleted":{"neq":1}},"order":"start-at ASC, weight ASC","limit":30}
@AutoValue
public abstract class Filter {
    abstract String getOrder();

    abstract int getLimit();

    abstract Where getWhere();

    abstract int getOffset();

    public static Filter create(int limit, int offset) {
        return new AutoValue_Filter("start-at ASC, weight ASC", limit, Where.create(), offset);
    }

    @Override
    public String toString() {
        return "{"
                + "\"order\":\"" + getOrder() + "\", "
                + "\"limit\":" + getLimit() + ", "
                + "\"where\":" + getWhere() + ", "
                + "\"offset\":" + getOffset()
                + "}";
    }
}
