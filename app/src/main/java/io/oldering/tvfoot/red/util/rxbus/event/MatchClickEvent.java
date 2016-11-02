package io.oldering.tvfoot.red.util.rxbus.event;


import io.oldering.tvfoot.red.viewmodel.MatchViewModel;

public class MatchClickEvent {
    MatchViewModel matchVM;

    public MatchClickEvent(MatchViewModel matchVM) {
        this.matchVM = matchVM;
    }

    public MatchViewModel getMatchVM() {
        return matchVM;
    }
}