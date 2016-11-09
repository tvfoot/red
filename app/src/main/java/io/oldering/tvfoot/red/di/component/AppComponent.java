package io.oldering.tvfoot.red.di.component;


import javax.inject.Singleton;

import dagger.Component;
import io.oldering.tvfoot.red.RedApp;
import io.oldering.tvfoot.red.di.module.ActivityModule;
import io.oldering.tvfoot.red.di.module.AppModule;
import io.oldering.tvfoot.red.di.module.NetworkingModule;
import io.oldering.tvfoot.red.di.module.RxBusModule;
import io.oldering.tvfoot.red.di.module.SchedulerModule;
import io.oldering.tvfoot.red.di.module.ServiceModule;

@Singleton
@Component(
        modules = {
                AppModule.class,
                NetworkingModule.class,
                ServiceModule.class,
                SchedulerModule.class,
                RxBusModule.class
        }
)
public interface AppComponent {
    void inject(RedApp boxBeeApplication);

    ActivityComponent plus(ActivityModule activityModule);
}