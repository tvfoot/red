package io.oldering.tvfoot.red.di.module;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import io.oldering.tvfoot.red.di.component.MainActivitySubComponent;
import io.oldering.tvfoot.red.util.MainActivity;

@Module(subcomponents = MainActivitySubComponent.class) //
public abstract class MainActivityModule {
  @Binds @IntoMap @ActivityKey(MainActivity.class) //
  abstract AndroidInjector.Factory<? extends Activity> bindMainActivityInjectorFactory(
      MainActivitySubComponent.Builder builder);
}
