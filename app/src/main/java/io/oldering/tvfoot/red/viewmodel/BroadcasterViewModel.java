package io.oldering.tvfoot.red.viewmodel;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import io.oldering.tvfoot.red.model.Broadcaster;

@AutoValue
public abstract class BroadcasterViewModel implements Parcelable {
    public abstract String getCode();

    public static BroadcasterViewModel create(Broadcaster broadcaster) {
        return new AutoValue_BroadcasterViewModel(broadcaster.getCode());
    }
}
