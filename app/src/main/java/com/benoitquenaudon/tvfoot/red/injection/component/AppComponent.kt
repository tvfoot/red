package com.benoitquenaudon.tvfoot.red.injection.component

import com.benoitquenaudon.tvfoot.red.RedApp
import com.benoitquenaudon.tvfoot.red.injection.module.ActivityBindingModule
import com.benoitquenaudon.tvfoot.red.injection.module.AppModule
import com.benoitquenaudon.tvfoot.red.injection.module.BaseImplementationModule
import com.benoitquenaudon.tvfoot.red.injection.module.LibrariesModule
import com.benoitquenaudon.tvfoot.red.injection.module.NetworkModule
import com.benoitquenaudon.tvfoot.red.injection.module.PreferenceServiceModule
import com.benoitquenaudon.tvfoot.red.injection.module.RxFactoryModule
import com.benoitquenaudon.tvfoot.red.injection.module.SchedulerModule
import com.benoitquenaudon.tvfoot.red.injection.module.ServiceBindingModule
import com.benoitquenaudon.tvfoot.red.injection.module.ServiceModule
import com.benoitquenaudon.tvfoot.red.injection.module.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
  AndroidSupportInjectionModule::class,
  ActivityBindingModule::class,
  ServiceBindingModule::class,
  AppModule::class,
  NetworkModule::class,
  ServiceModule::class,
  ViewModelModule::class,
  PreferenceServiceModule::class,
  SchedulerModule::class,
  RxFactoryModule::class,
  LibrariesModule::class,
  BaseImplementationModule::class])
interface AppComponent : AndroidInjector<RedApp> {

  @Component.Builder
  abstract class Builder : AndroidInjector.Builder<RedApp>()
}