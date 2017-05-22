package com.benoitquenaudon.tvfoot.red.app.common.schedulers;

import com.benoitquenaudon.tvfoot.red.util.MethodsAreNonnullByDefault;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Implementation of the {@link BaseSchedulerProvider} making all {@link Scheduler}s immediate.
 */
@MethodsAreNonnullByDefault public class ImmediateSchedulerProvider
    implements BaseSchedulerProvider {

  @Override public Scheduler computation() {
    return Schedulers.trampoline();
  }

  @Override public Scheduler io() {
    return Schedulers.trampoline();
  }

  @Override public Scheduler ui() {
    return Schedulers.trampoline();
  }
}
