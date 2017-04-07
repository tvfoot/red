package io.oldering.tvfoot.red.di.component;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import io.oldering.tvfoot.red.di.module.MainActivityInnerModule;
import io.oldering.tvfoot.red.util.MainActivity;

@Subcomponent(modules = {
    MainActivityInnerModule.class, //
}) public interface MainActivitySubComponent extends AndroidInjector<MainActivity> {
  @Subcomponent.Builder abstract class Builder extends AndroidInjector.Builder<MainActivity> {
  }
}