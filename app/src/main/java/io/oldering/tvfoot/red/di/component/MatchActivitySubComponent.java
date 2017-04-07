package io.oldering.tvfoot.red.di.component;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import io.oldering.tvfoot.red.di.module.MatchActivityInnerModule;
import io.oldering.tvfoot.red.match.MatchActivity;

@Subcomponent(modules = {
    MatchActivityInnerModule.class, //
}) public interface MatchActivitySubComponent extends AndroidInjector<MatchActivity> {
  @Subcomponent.Builder abstract class Builder extends AndroidInjector.Builder<MatchActivity> {
  }
}