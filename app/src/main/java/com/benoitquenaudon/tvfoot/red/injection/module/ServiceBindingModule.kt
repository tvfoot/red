package com.benoitquenaudon.tvfoot.red.injection.module

import com.benoitquenaudon.tvfoot.red.app.domain.match.job.MatchNotificationSchedulerService
import com.benoitquenaudon.tvfoot.red.app.domain.match.job.MatchReminderService
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ServiceBindingModule {
  @ContributesAndroidInjector
  abstract fun contributeMatchNotificationSchedulerServiceInjector(): MatchNotificationSchedulerService

  @ContributesAndroidInjector
  abstract fun contributeMatchReminderServiceInjector(): MatchReminderService
}