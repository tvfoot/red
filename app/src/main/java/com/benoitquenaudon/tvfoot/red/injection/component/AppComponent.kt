package com.benoitquenaudon.tvfoot.red.injection.component

import com.benoitquenaudon.tvfoot.red.RedApp
import com.benoitquenaudon.tvfoot.red.app.domain.match.job.MatchNotificationSchedulerService
import com.benoitquenaudon.tvfoot.red.app.domain.match.job.MatchReminderService
import com.benoitquenaudon.tvfoot.red.injection.module.ActivityModule
import com.benoitquenaudon.tvfoot.red.injection.module.AppModule
import com.benoitquenaudon.tvfoot.red.injection.module.BaseImplementationModule
import com.benoitquenaudon.tvfoot.red.injection.module.FirebaseModule
import com.benoitquenaudon.tvfoot.red.injection.module.LibrariesModule
import com.benoitquenaudon.tvfoot.red.injection.module.NetworkModule
import com.benoitquenaudon.tvfoot.red.injection.module.PreferenceServiceModule
import com.benoitquenaudon.tvfoot.red.injection.module.RxFactoryModule
import com.benoitquenaudon.tvfoot.red.injection.module.SchedulerModule
import com.benoitquenaudon.tvfoot.red.injection.module.ServiceModule
import com.benoitquenaudon.tvfoot.red.injection.module.ViewModelModule
import com.squareup.picasso.Picasso
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    AppModule::class,
    NetworkModule::class,
    ServiceModule::class,
    ViewModelModule::class,
    PreferenceServiceModule::class,
    SchedulerModule::class,
    RxFactoryModule::class,
    FirebaseModule::class,
    LibrariesModule::class,
    BaseImplementationModule::class))
interface AppComponent {
  fun plus(activityModule: ActivityModule): ActivityComponent
  fun inject(redApp: RedApp)
  fun inject(matchReminderService: MatchReminderService)
  fun inject(matchNotificationSchedulerService: MatchNotificationSchedulerService)
}