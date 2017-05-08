package io.oldering.tvfoot.red.app.injection.component;

import com.squareup.picasso.Picasso;
import dagger.Component;
import io.oldering.tvfoot.red.RedApp;
import io.oldering.tvfoot.red.app.injection.module.AppModule;
import io.oldering.tvfoot.red.app.injection.module.FirebaseModule;
import io.oldering.tvfoot.red.app.injection.module.NetworkModule;
import io.oldering.tvfoot.red.app.injection.module.PreferenceServiceModule;
import io.oldering.tvfoot.red.app.injection.module.RxFactoryModule;
import io.oldering.tvfoot.red.app.injection.module.SchedulerModule;
import io.oldering.tvfoot.red.app.injection.module.ServiceModule;
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

  OkHttpClient okHttpClient();

  Picasso picasso();
}