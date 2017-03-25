package io.oldering.tvfoot.red.di.component;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import io.oldering.tvfoot.red.di.module.ActivityModule;
import io.oldering.tvfoot.red.util.MainActivity;

@Subcomponent(modules = {
    ActivityModule.class, //
}) public interface MainActivitySubComponent extends AndroidInjector<MainActivity> {
  @Subcomponent.Builder abstract class Builder extends AndroidInjector.Builder<MainActivity> {
    public abstract Builder activityModule(ActivityModule activityModule);

    @Override public void seedInstance(MainActivity instance) {
      activityModule(new ActivityModule(instance));
    }
  }
}