package io.oldering.tvfoot.red.di.component;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import io.oldering.tvfoot.red.di.module.ActivityModule;
import io.oldering.tvfoot.red.match.MatchActivity;

@Subcomponent(modules = {
    ActivityModule.class, //
}) public interface MatchActivitySubComponent extends AndroidInjector<MatchActivity> {
  @Subcomponent.Builder abstract class Builder extends AndroidInjector.Builder<MatchActivity> {
    public abstract Builder activityModule(ActivityModule activityModule);

    @Override public void seedInstance(MatchActivity instance) {
      activityModule(new ActivityModule(instance));
    }
  }
}