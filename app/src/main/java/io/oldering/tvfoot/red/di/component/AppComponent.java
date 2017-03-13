package io.oldering.tvfoot.red.di.component;

import dagger.Component;
import io.oldering.tvfoot.red.di.module.ActivityModule;
import io.oldering.tvfoot.red.di.module.AppModule;
import io.oldering.tvfoot.red.di.module.NetworkModule;
import io.oldering.tvfoot.red.di.module.SchedulerModule;
import io.oldering.tvfoot.red.di.module.ServiceModule;
import javax.inject.Singleton;

@Singleton @Component(modules = {
    AppModule.class, //
    NetworkModule.class, //
    ServiceModule.class, //
    SchedulerModule.class, //
}) public interface AppComponent {
  ActivityComponent plus(ActivityModule activityModule);
}