package com.benoitquenaudon.tvfoot.red.app.injection.module;

import android.app.Application;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import com.benoitquenaudon.tvfoot.red.BuildConfig;
import com.benoitquenaudon.tvfoot.red.api.TvfootService;
import com.benoitquenaudon.tvfoot.red.app.data.entity.AutoValueGsonTypeAdapterFactory;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

@Module public class NetworkModule {
  @Provides @Singleton static Gson provideGson() {
    return new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonTypeAdapterFactory.create())
        .create();
  }

  @Provides @Singleton static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
    return new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? BODY : NONE);
  }

  @Provides @Singleton
  static OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
    return new OkHttpClient.Builder().addNetworkInterceptor(httpLoggingInterceptor).build();
  }

  @Provides @Singleton static Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
    return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .baseUrl(TvfootService.Companion.getBASE_URL())
        .build();
  }

  @Provides @Singleton
  static OkHttp3Downloader provideOkHttp3Downloader(OkHttpClient okHttpClient) {
    return new OkHttp3Downloader(okHttpClient);
  }

  @Provides @Singleton
  static Picasso providePicasso(Application context, OkHttp3Downloader okHttp3Downloader) {
    return new Picasso.Builder(context).downloader(okHttp3Downloader).build();
  }
}
