package com.benoitquenaudon.tvfoot.red.app.injection.module

import com.benoitquenaudon.tvfoot.red.api.TvfootService
import com.benoitquenaudon.tvfoot.red.testutil.FakeTvfootService
import com.benoitquenaudon.tvfoot.red.testutil.Fixture
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module object ServiceMockModule {
  @JvmStatic @Provides @Singleton fun provideMatchService(fixture: Fixture): TvfootService {
    return FakeTvfootService(fixture)
  }
}