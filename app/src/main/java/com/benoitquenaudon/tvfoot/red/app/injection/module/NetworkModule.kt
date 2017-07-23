package com.benoitquenaudon.tvfoot.red.app.injection.module

import android.app.Application
import com.benoitquenaudon.tvfoot.red.BuildConfig
import com.benoitquenaudon.tvfoot.red.api.TvfootService
import com.benoitquenaudon.tvfoot.red.app.data.entity.AutoValueGsonTypeAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.picasso.OkHttp3Downloader
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module object NetworkModule {

  @JvmStatic @Provides @Singleton
  fun provideGson(): Gson {
    return GsonBuilder().registerTypeAdapterFactory(AutoValueGsonTypeAdapterFactory.create())
        .create()
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
  fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
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
