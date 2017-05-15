package com.benoitquenaudon.tvfoot.red.app.common.schedulers;

import android.support.annotation.NonNull;
import io.reactivex.Scheduler;

/**
 * Allow providing different types of {@link Scheduler}s.
 */
public interface BaseSchedulerProvider {

  @NonNull Scheduler computation();

  @NonNull Scheduler io();

  @NonNull Scheduler ui();
}
