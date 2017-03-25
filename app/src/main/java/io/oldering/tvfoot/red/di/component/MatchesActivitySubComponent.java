package io.oldering.tvfoot.red.di.component;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import io.oldering.tvfoot.red.di.module.ActivityModule;
import io.oldering.tvfoot.red.matches.MatchesActivity;

@Subcomponent(modules = {
    ActivityModule.class, //
}) public interface MatchesActivitySubComponent extends AndroidInjector<MatchesActivity> {
  @Subcomponent.Builder abstract class Builder extends AndroidInjector.Builder<MatchesActivity> {
    public abstract Builder activityModule(ActivityModule activityModule);

    @Override public void seedInstance(MatchesActivity instance) {
      activityModule(new ActivityModule(instance));
    }
  }
}