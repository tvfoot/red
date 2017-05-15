package com.benoitquenaudon.tvfoot.red.app.injection.component;

import dagger.Component;
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider;
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchService;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesService;
import com.benoitquenaudon.tvfoot.red.app.injection.module.NetworkModule;
import com.benoitquenaudon.tvfoot.red.app.injection.module.ServiceMockModule;
import com.benoitquenaudon.tvfoot.red.app.injection.module.TestSchedulerModule;
import com.benoitquenaudon.tvfoot.red.util.Fixture;
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
