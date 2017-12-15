package com.benoitquenaudon.tvfoot.red.injection.module

import android.app.Application
import com.benoitquenaudon.tvfoot.red.injection.qualifiers.NowAtStartup
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

@Module
class AppModule(val application: Application) {
  @Provides
  @NowAtStartup
  fun nowAtStartup(): Long = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(3)

  @Provides
  fun provideApplication(): Application {
    return application
  }

  @Provides
  fun provideCompositeDisposable(): CompositeDisposable {
    return CompositeDisposable()
  }
}
