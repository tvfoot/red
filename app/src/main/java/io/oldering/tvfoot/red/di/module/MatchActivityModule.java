package io.oldering.tvfoot.red.di.module;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import io.oldering.tvfoot.red.di.component.MatchActivitySubComponent;
import io.oldering.tvfoot.red.match.MatchActivity;

@Module(subcomponents = MatchActivitySubComponent.class) //
public abstract class MatchActivityModule {
  @Binds @IntoMap @ActivityKey(MatchActivity.class) //
  abstract AndroidInjector.Factory<? extends Activity> bindMatchActivityInjectorFactory(
      MatchActivitySubComponent.Builder builder);
}
