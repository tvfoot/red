package io.oldering.tvfoot.red.util.rxbus.event;

import com.google.auto.value.AutoValue;

import io.oldering.tvfoot.red.viewmodel.MatchViewModel;

@AutoValue
public abstract class MatchClickEvent {
    public abstract MatchViewModel getMatchVM();

    public static MatchClickEvent create(MatchViewModel matchVM) {
        return new AutoValue_MatchClickEvent(matchVM);
    }
}