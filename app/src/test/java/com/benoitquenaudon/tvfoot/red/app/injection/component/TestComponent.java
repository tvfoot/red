package com.benoitquenaudon.tvfoot.red.app.injection.component;

import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchStateBinderTest;
import dagger.Component;
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider;
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchRepository;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesService;
import com.benoitquenaudon.tvfoot.red.app.injection.module.NetworkModule;
import com.benoitquenaudon.tvfoot.red.app.injection.module.ServiceMockModule;
import com.benoitquenaudon.tvfoot.red.app.injection.module.TestSchedulerModule;
import com.benoitquenaudon.tvfoot.red.testutil.Fixture;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton @Component(modules = {
    NetworkModule.class, //
    ServiceMockModule.class, //
    TestSchedulerModule.class, //
}) public interface TestComponent {
  Fixture fixture();

  BaseSchedulerProvider schedulerProvider();

  MatchesService matchesRepository();

  MatchRepository matchRepository();

  void inject(@NotNull MatchStateBinderTest matchStateBinderTest);
}
