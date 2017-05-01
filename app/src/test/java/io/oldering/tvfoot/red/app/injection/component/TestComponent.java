package io.oldering.tvfoot.red.app.injection.component;

import dagger.Component;
import io.oldering.tvfoot.red.app.common.schedulers.BaseSchedulerProvider;
import io.oldering.tvfoot.red.app.domain.match.state.MatchService;
import io.oldering.tvfoot.red.app.domain.matches.state.MatchesService;
import io.oldering.tvfoot.red.app.injection.module.NetworkModule;
import io.oldering.tvfoot.red.app.injection.module.ServiceMockModule;
import io.oldering.tvfoot.red.app.injection.module.TestSchedulerModule;
import io.oldering.tvfoot.red.util.Fixture;
import javax.inject.Singleton;

@Singleton @Component(modules = {
    NetworkModule.class, //
    ServiceMockModule.class, //
    TestSchedulerModule.class, //
}) public interface TestComponent {
  Fixture fixture();

  BaseSchedulerProvider schedulerProvider();

  MatchesService matchesInteractor();

  MatchService matchInteractor();
}
