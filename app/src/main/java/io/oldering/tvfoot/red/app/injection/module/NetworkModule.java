package io.oldering.tvfoot.red.app.injection.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.BuildConfig;
import io.oldering.tvfoot.red.api.TvfootService;
import io.oldering.tvfoot.red.app.data.entity.AutoValueGsonTypeAdapterFactory;
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
        .baseUrl(TvfootService.BASE_URL)
        .build();
  }
}
