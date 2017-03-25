package io.oldering.tvfoot.red.di.module;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import io.oldering.tvfoot.red.di.component.MatchesActivitySubComponent;
import io.oldering.tvfoot.red.matches.MatchesActivity;

@Module(subcomponents = MatchesActivitySubComponent.class) //
public abstract class MatchesActivityModule {
  @Binds @IntoMap @ActivityKey(MatchesActivity.class) //
  abstract AndroidInjector.Factory<? extends Activity> bindMatchesActivityInjectorFactory(
      MatchesActivitySubComponent.Builder builder);
}
