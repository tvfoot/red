package io.oldering.tvfoot.red.app.injection.module;

import android.app.Activity;
import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.app.injection.scope.ActivityScope;
import io.reactivex.disposables.CompositeDisposable;

@Module public class ActivityModule {
  private Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides @ActivityScope Activity provideActivity() {
    return activity;
  }

  @Provides CompositeDisposable provideCompositeDisposable() {
    return new CompositeDisposable();
  }
}