package io.oldering.tvfoot.red.model.search;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Where {
    abstract Deleted getDeleted();

    static Where create() {
        return new AutoValue_Where(Deleted.create());
    }

    @Override
    public String toString() {
        return "{"
                + "\"deleted\":" + getDeleted()
                + "}";
    }
}
