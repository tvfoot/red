package io.oldering.tvfoot.red.app.injection.module;

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
}
