package io.oldering.tvfoot.red.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;

import io.oldering.tvfoot.red.model.AutoValueGsonTypeAdapterFactory;
import io.oldering.tvfoot.red.model.Match;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MatchService {
    // e.g.: filter:{"where":{"deleted":{"neq":1}},"order":"start-at ASC, weight ASC","limit":30}
    @GET("/api/matches/findFuture")
    Single<List<Match>> findFuture(@Query("filter") String filter);

    // e.g.: filter:{"where":{"start-at":{"gte":"2016-09-21T00:00:00.072Z"},"deleted":{"neq":1}},"order":"start-at ASC, weight ASC","limit":1500}
    @GET("/api/matches")
    Single<List<Match>> get(@Query("filter") String filter);

}