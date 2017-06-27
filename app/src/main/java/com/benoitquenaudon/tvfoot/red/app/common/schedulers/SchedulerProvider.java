package com.benoitquenaudon.tvfoot.red.app.common.schedulers;

import android.support.annotation.NonNull;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Singleton;

/**
 * Provides different types of schedulers.
 */
@Singleton public class SchedulerProvider implements BaseSchedulerProvider {
  @NonNull @Override public Scheduler computation() {
    return Schedulers.computation();
  }

  @NonNull @Override public Scheduler io() {
    return Schedulers.io();
  }

  @NonNull @Override public Scheduler ui() {
    return AndroidSchedulers.mainThread();
  }
}
