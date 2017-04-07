package io.oldering.tvfoot.red.di.module;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;
import io.oldering.tvfoot.red.util.MainActivity;

@Module public abstract class MainActivityInnerModule {
  @Binds abstract Activity bindActivity(MainActivity activity);
}
