package io.oldering.tvfoot.red.di;

import dagger.Subcomponent;
import io.oldering.tvfoot.red.view.MatchDetailActivity;
import io.oldering.tvfoot.red.view.MatchListActivity;

@ScopeActivity
@Subcomponent(modules = {ActivityModule.class, BundleModule.class})
public interface ActivityComponent {
    void inject(MatchListActivity matchListActivity);

    void inject(MatchDetailActivity matchDetailActivity);
}
