package com.benoitquenaudon.tvfoot.red.app.common.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

/**
 * Implementation of the [BaseSchedulerProvider] making all [Scheduler]s immediate.
 */
@Singleton
class ImmediateSchedulerProvider : BaseSchedulerProvider {
  override fun computation(): Scheduler {
    return Schedulers.trampoline()
  }

  override fun io(): Scheduler {
    return Schedulers.trampoline()
  }

  override fun ui(): Scheduler {
    return Schedulers.trampoline()
  }
}
