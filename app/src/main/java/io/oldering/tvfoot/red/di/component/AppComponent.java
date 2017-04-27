package io.oldering.tvfoot.red.di.component;

import dagger.Component;
import io.oldering.tvfoot.red.RedApp;
import io.oldering.tvfoot.red.di.module.AppModule;
import io.oldering.tvfoot.red.di.module.NetworkModule;
import io.oldering.tvfoot.red.di.module.RxFactoryModule;
import io.oldering.tvfoot.red.di.module.SchedulerModule;
import io.oldering.tvfoot.red.di.module.ServiceModule;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;

@Singleton @Component(modules = {
    AppModule.class, //
    NetworkModule.class, //
    ServiceModule.class, //
    SchedulerModule.class, //
    RxFactoryModule.class, //
}) public interface AppComponent {
  ScreenComponent screenComponent();

  void inject(RedApp redApp);

  OkHttpClient okHttpClient();
}