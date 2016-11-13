package io.oldering.tvfoot.red.di.component;

import javax.inject.Singleton;

import dagger.Component;
import io.oldering.tvfoot.red.di.module.NetworkModule;
import io.oldering.tvfoot.red.di.module.RxBusModule;
import io.oldering.tvfoot.red.di.module.SchedulerModule;
import io.oldering.tvfoot.red.di.module.ServiceModule;
import io.oldering.tvfoot.red.util.Fixture;
import io.oldering.tvfoot.red.viewmodel.MatchListViewModel;

@Singleton
@Component(
        modules = {
                NetworkModule.class,
                ServiceModule.class,
                SchedulerModule.class,
                RxBusModule.class
        }
)
public interface TestComponent {
    MatchListViewModel matchListViewModel();

    Fixture fixture();
}
