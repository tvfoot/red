package com.benoitquenaudon.tvfoot.red.app.injection.module

import com.benoitquenaudon.tvfoot.red.app.domain.libraries.Library
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module object LibrariesModule {

  @JvmStatic @Provides @Singleton
  fun provideLibraries(): List<Library> {
    return arrayListOf(
        Library("Android support libraries",
            "The Android support libraries offer a number of features that are not built into the framework.",
            "https://developer.android.com/topic/libraries/support-library",
            "https://developer.android.com/images/android_icon_125.png",
            false),
        Library("OkHttp",
            "An HTTP & HTTP/2 client for Android and Java applications.",
            "http://square.github.io/okhttp/",
            "https://avatars.githubusercontent.com/u/82592",
            false),
        Library("Retrofit",
            "A type-safe HTTP client for Android and Java.",
            "http://square.github.io/retrofit/",
            "https://avatars.githubusercontent.com/u/82592",
            false)
    )
  }
}
