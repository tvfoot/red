package com.benoitquenaudon.tvfoot.red.app.injection.module

import android.app.Application
import com.benoitquenaudon.tvfoot.red.BuildConfig
import com.benoitquenaudon.tvfoot.red.api.TvfootService
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import javax.inject.Singleton

@Module object NetworkModule {

  @JvmStatic @Provides @Singleton
  fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build()
  }

  @JvmStatic @Provides @Singleton
  fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) BODY else NONE)
  }

  @JvmStatic @Provides @Singleton
  fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder().addNetworkInterceptor(httpLoggingInterceptor).build()
  }

  @JvmStatic @Provides @Singleton
  fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .baseUrl(TvfootService.BASE_URL)
        .build()
  }

  @JvmStatic @Provides @Singleton
  fun provideOkHttp3Downloader(okHttpClient: OkHttpClient): OkHttp3Downloader {
    return OkHttp3Downloader(okHttpClient)
  }

  @JvmStatic @Provides @Singleton
  fun providePicasso(context: Application, okHttp3Downloader: OkHttp3Downloader): Picasso {
    return Picasso.Builder(context).downloader(okHttp3Downloader).build()
  }
}
