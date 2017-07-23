package com.benoitquenaudon.tvfoot.red.app.injection.module

import android.app.Application
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module class AppModule(val application: Application) {

  @Provides fun provideApplication(): Application {
    return application
  }

  @Provides fun provideCompositeDisposable(): CompositeDisposable {
    return CompositeDisposable()
  }
}
