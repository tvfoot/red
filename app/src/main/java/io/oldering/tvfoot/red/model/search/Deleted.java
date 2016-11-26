package io.oldering.tvfoot.red.model.search;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Deleted {
    // 0 or 1
    abstract int getNeq();

    static Deleted create() {
        return new AutoValue_Deleted(1);
    }

    @Override
    public String toString() {
        return "{"
                + "\"neq\":" + getNeq()
                + "}";
    }
}
