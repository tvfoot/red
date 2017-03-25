package io.oldering.tvfoot.red.di.module;

import android.app.Application;
import dagger.Module;
import dagger.Provides;

@Module public class AppModule {
  protected final Application application;

  public AppModule(Application application) {
    this.application = application;
  }

  @Provides Application provideApplication() {
    return application;
  }

  //@Provides @Singleton @Named("application") Context provideContext() {
  //  return application;
  //}
}
