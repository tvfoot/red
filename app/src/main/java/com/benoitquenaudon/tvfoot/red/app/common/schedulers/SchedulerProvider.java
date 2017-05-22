package com.benoitquenaudon.tvfoot.red.app.common.schedulers;

import com.benoitquenaudon.tvfoot.red.util.MethodsAreNonnullByDefault;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Singleton;

/**
 * Provides different types of schedulers.
 */
@MethodsAreNonnullByDefault @Singleton public class SchedulerProvider
    implements BaseSchedulerProvider {

  @Override public Scheduler computation() {
    return Schedulers.computation();
  }

  @Override public Scheduler io() {
    return Schedulers.io();
  }

  @Override public Scheduler ui() {
    return AndroidSchedulers.mainThread();
  }
}
