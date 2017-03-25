package io.oldering.tvfoot.red.di.component;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import io.oldering.tvfoot.red.RedApp;
import io.oldering.tvfoot.red.di.module.AppModule;
import io.oldering.tvfoot.red.di.module.MainActivityModule;
import io.oldering.tvfoot.red.di.module.MatchActivityModule;
import io.oldering.tvfoot.red.di.module.MatchesActivityModule;
import io.oldering.tvfoot.red.di.module.NetworkModule;
import io.oldering.tvfoot.red.di.module.SchedulerModule;
import io.oldering.tvfoot.red.di.module.ServiceModule;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;

@Singleton @Component(modules = {
    AppModule.class, //
    NetworkModule.class, //
    ServiceModule.class, //
    SchedulerModule.class, //
    AndroidInjectionModule.class, //
    MainActivityModule.class, //
    MatchActivityModule.class, //
    MatchesActivityModule.class, //
}) public interface AppComponent {
  void inject(RedApp redApp);

  //ActivityComponent plus(ActivityModule activityModule);

  OkHttpClient okHttpClient();
}