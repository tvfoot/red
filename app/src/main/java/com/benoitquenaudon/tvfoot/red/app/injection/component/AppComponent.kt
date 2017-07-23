package com.benoitquenaudon.tvfoot.red.app.injection.component

import com.benoitquenaudon.tvfoot.red.RedApp
import com.benoitquenaudon.tvfoot.red.app.domain.match.job.MatchReminderService
import com.benoitquenaudon.tvfoot.red.app.injection.module.AppModule
import com.benoitquenaudon.tvfoot.red.app.injection.module.FirebaseModule
import com.benoitquenaudon.tvfoot.red.app.injection.module.NetworkModule
import com.benoitquenaudon.tvfoot.red.app.injection.module.PreferenceServiceModule
import com.benoitquenaudon.tvfoot.red.app.injection.module.RxFactoryModule
import com.benoitquenaudon.tvfoot.red.app.injection.module.SchedulerModule
import com.benoitquenaudon.tvfoot.red.app.injection.module.ServiceModule
import com.squareup.picasso.Picasso
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    AppModule::class,
    NetworkModule::class,
    ServiceModule::class,
    PreferenceServiceModule::class,
    SchedulerModule::class,
    RxFactoryModule::class,
    FirebaseModule::class))
interface AppComponent {
  fun screenComponent(): ScreenComponent

  fun inject(redApp: RedApp)

  fun inject(matchReminderService: MatchReminderService)

  fun okHttpClient(): OkHttpClient

  fun picasso(): Picasso
}