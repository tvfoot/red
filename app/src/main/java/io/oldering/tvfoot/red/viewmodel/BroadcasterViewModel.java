package io.oldering.tvfoot.red.viewmodel;

import com.google.auto.value.AutoValue;

import io.oldering.tvfoot.red.model.Broadcaster;

@AutoValue
public abstract class BroadcasterViewModel {
    public abstract String getCode();

    public static BroadcasterViewModel create(Broadcaster broadcaster) {
        return new AutoValue_BroadcasterViewModel(broadcaster.getCode());
    }
}
