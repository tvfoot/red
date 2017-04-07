package io.oldering.tvfoot.red.di.module;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;
import io.oldering.tvfoot.red.match.MatchActivity;

@Module public abstract class MatchActivityInnerModule {
  @Binds abstract Activity bindActivity(MatchActivity activity);
}
