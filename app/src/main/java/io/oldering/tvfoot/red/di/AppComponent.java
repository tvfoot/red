package io.oldering.tvfoot.red.di;


import javax.inject.Singleton;

import dagger.Component;
import io.oldering.tvfoot.red.view.MatchListActivity;
import io.oldering.tvfoot.red.viewmodel.MatchListViewModel;

@Singleton
@Component(
        modules = {
                NetworkingModule.class,
                ServiceModule.class,
                SchedulerModule.class,
                RxBusModule.class
        }
)
public interface AppComponent {
    void inject(MatchListActivity matchListActivity);

    MatchListViewModel matchListVM();

    ActivityComponent plusActivityComponent(ActivityModule activityModule);
}