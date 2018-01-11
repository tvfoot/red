package com.benoitquenaudon.tvfoot.red.injection.module

import android.app.Activity
import com.benoitquenaudon.tvfoot.red.app.domain.libraries.LibrariesActivity
import dagger.Binds
import dagger.Module

@Module
abstract class LibrariesActivityModule {
  @Binds abstract fun provideActivity(activity: LibrariesActivity): Activity
}