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
                SchedulerModule.class
        }
)
public interface AppComponent {
        void inject(MatchListActivity matchListActivity);

        void inject(MatchListViewModel matchListViewModel);
}