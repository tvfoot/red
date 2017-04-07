package io.oldering.tvfoot.red.di.component;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import io.oldering.tvfoot.red.di.module.MatchesActivityInnerModule;
import io.oldering.tvfoot.red.matches.MatchesActivity;

@Subcomponent(modules = {
    MatchesActivityInnerModule.class, //
}) public interface MatchesActivitySubComponent extends AndroidInjector<MatchesActivity> {
  @Subcomponent.Builder abstract class Builder extends AndroidInjector.Builder<MatchesActivity> {
  }
}