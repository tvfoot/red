package io.oldering.tvfoot.red.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.model.AutoValueGsonTypeAdapterFactory;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module public class NetworkModule {
  private String baseUrl;

  public NetworkModule(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  @Provides @Singleton Gson provideGson() {
    return new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonTypeAdapterFactory.create())
        .create();
  }

  @Provides @Singleton HttpLoggingInterceptor provideHttpLoggingInterceptor() {
    return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
  }

  @Provides @Singleton OkHttpClient provideOkHttpClient(
      HttpLoggingInterceptor httpLoggingInterceptor) {
    return new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
        .addNetworkInterceptor(httpLoggingInterceptor)
        .build();
  }

  @Provides @Singleton Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
    return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .build();
  }
}
