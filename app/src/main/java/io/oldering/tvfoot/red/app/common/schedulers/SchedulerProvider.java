package io.oldering.tvfoot.red.app.common.schedulers;

import android.support.annotation.NonNull;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Singleton;

/**
 * Provides different types of schedulers.
 */
@Singleton public class SchedulerProvider implements BaseSchedulerProvider {

  @Override @NonNull public Scheduler computation() {
    return Schedulers.computation();
  }

  @Override @NonNull public Scheduler io() {
    return Schedulers.io();
  }

  @Override @NonNull public Scheduler ui() {
    return AndroidSchedulers.mainThread();
  }
}
