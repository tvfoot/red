package com.benoitquenaudon.tvfoot.red.injection.module

import com.benoitquenaudon.tvfoot.red.injection.qualifiers.NowAtStartup
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

@Module
object AppModule {
  @JvmStatic
  @Provides
  @NowAtStartup
  fun nowAtStartup(): Long = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(3)

  @JvmStatic
  @Provides
  fun provideCompositeDisposable(): CompositeDisposable {
    return CompositeDisposable()
  }
}
