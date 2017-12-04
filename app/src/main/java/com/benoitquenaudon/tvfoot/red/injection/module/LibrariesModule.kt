package com.benoitquenaudon.tvfoot.red.injection.module

import com.benoitquenaudon.tvfoot.red.app.domain.libraries.Library
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LibrariesModule {

  @JvmStatic
  @Provides
  @Singleton
  fun provideLibraries(): List<Library> {
    return arrayListOf(
        Library("Android support libraries",
            "The Android support libraries offer a number of features that are not built into the framework.",
            "https://developer.android.com/topic/libraries/support-library",
            "https://developer.android.com/images/android_icon_125.png",
            false),
        Library("AutoValue: Parcel Extension",
            "An extension for Google's AutoValue that supports Android's Parcelable interface.",
            "https://github.com/rharter/auto-value-parcel",
            "https://avatars2.githubusercontent.com/u/1296750",
            true),
        Library("Dagger",
            "A fast dependency injector for Android and Java.",
            "https://github.com/google/dagger",
            "https://avatars2.githubusercontent.com/u/1342004",
            false),
        Library("LeakCanary",
            "A memory leak detection library for Android and Java.",
            "https://github.com/square/leakcanary",
            "https://avatars.githubusercontent.com/u/82592",
            false),
        Library("Moshi",
            "A modern JSON library for Android and Java.",
            "https://github.com/square/moshi",
            "https://avatars0.githubusercontent.com/u/82592",
            false),
        Library("OkHttp",
            "An HTTP & HTTP/2 client for Android and Java applications.",
            "http://square.github.io/okhttp/",
            "https://avatars.githubusercontent.com/u/82592",
            false),
        Library("Picasso",
            "A powerful image downloading and caching library for Android",
            "https://github.com/square/picasso/",
            "https://avatars.githubusercontent.com/u/82592",
            false),
        Library("Picasso 2 OkHttp 3 Downloader",
            "A OkHttp 3 downloader implementation for Picasso 2.",
            "https://github.com/JakeWharton/picasso2-okhttp3-downloader",
            "https://avatars2.githubusercontent.com/u/66577",
            true),
        Library("Retrofit",
            "A type-safe HTTP client for Android and Java.",
            "http://square.github.io/retrofit/",
            "https://avatars.githubusercontent.com/u/82592",
            false),
        Library("RxAndroid",
            "RxJava bindings for Android",
            "https://github.com/ReactiveX/RxAndroid",
            "https://avatars3.githubusercontent.com/u/6407041",
            false),
        Library("RxBinding",
            "RxJava binding APIs for Android's UI widgets.",
            "https://github.com/JakeWharton/RxBinding",
            "https://avatars0.githubusercontent.com/u/66577",
            true),
        Library("RxDataBinding",
            "RxJava2 binding APIs for Android's Data Binding Library.",
            "https://github.com/oldergod/RxDataBinding",
            "https://avatars3.githubusercontent.com/u/1767669",
            true),
        Library("RxJava",
            "Library for composing asynchronous and event-based programs using observable sequences for the Java VM.",
            "https://github.com/ReactiveX/RxJava",
            "https://avatars3.githubusercontent.com/u/6407041",
            false),
        Library("Timber",
            "A logger with a small, extensible API which provides utility on top of Android's normal Log class.",
            "https://github.com/JakeWharton/timber",
            "https://avatars2.githubusercontent.com/u/66577",
            true)
    )
  }
}
