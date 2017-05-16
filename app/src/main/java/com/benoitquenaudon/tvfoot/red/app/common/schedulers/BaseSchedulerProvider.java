package com.benoitquenaudon.tvfoot.red.app.common.schedulers;

import com.benoitquenaudon.tvfoot.red.util.MethodsAreNonnullByDefault;
import io.reactivex.Scheduler;

/**
 * Allow providing different types of {@link Scheduler}s.
 */
@MethodsAreNonnullByDefault public interface BaseSchedulerProvider {

  Scheduler computation();

  Scheduler io();

  Scheduler ui();
}
