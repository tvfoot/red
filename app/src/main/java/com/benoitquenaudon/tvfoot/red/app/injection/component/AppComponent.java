package com.benoitquenaudon.tvfoot.red.app.injection.component;

import com.benoitquenaudon.tvfoot.red.app.domain.match.job.MatchReminderService;
import com.squareup.picasso.Picasso;
import dagger.Component;
import com.benoitquenaudon.tvfoot.red.RedApp;
import com.benoitquenaudon.tvfoot.red.app.injection.module.AppModule;
import com.benoitquenaudon.tvfoot.red.app.injection.module.FirebaseModule;
import com.benoitquenaudon.tvfoot.red.app.injection.module.NetworkModule;
import com.benoitquenaudon.tvfoot.red.app.injection.module.PreferenceServiceModule;
import com.benoitquenaudon.tvfoot.red.app.injection.module.RxFactoryModule;
import com.benoitquenaudon.tvfoot.red.app.injection.module.SchedulerModule;
import com.benoitquenaudon.tvfoot.red.app.injection.module.ServiceModule;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;

@Singleton @Component(modules = {
    AppModule.class, //
    NetworkModule.class, //
    ServiceModule.class, //
    PreferenceServiceModule.class, //
    SchedulerModule.class, //
    RxFactoryModule.class, //
    FirebaseModule.class, //
}) public interface AppComponent {
  ScreenComponent screenComponent();

  void inject(RedApp redApp);

  void inject(MatchReminderService matchReminderService);

  OkHttpClient okHttpClient();

  Picasso picasso();

}