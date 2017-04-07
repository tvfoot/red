package io.oldering.tvfoot.red.di.module;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;
import io.oldering.tvfoot.red.matches.MatchesActivity;

@Module public abstract class MatchesActivityInnerModule {
  @Binds abstract Activity bindActivity(MatchesActivity activity);
}
