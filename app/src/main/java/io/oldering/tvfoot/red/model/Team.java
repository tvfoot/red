package io.oldering.tvfoot.red.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class Team {
    public static final String DEFAULT_CODE = "default";

    @Nullable
    public abstract String getCode();

    @Nullable
    public abstract String getName();

    @Nullable
    public abstract String getFullname();

    @Nullable
    public abstract String getCity();

    @Nullable
    public abstract String getCountry();

    @Nullable
    public abstract String getUrl();

    @Nullable
    public abstract String getStadium();

    @Nullable
    public abstract String getTwitter();

    @Nullable
    public abstract String getType();

    public static TypeAdapter<Team> typeAdapter(Gson gson) {
        return new AutoValue_Team.GsonTypeAdapter(gson);
    }

    public static Team create(@Nullable String code,
                              @Nullable String name,
                              @Nullable String fullname,
                              @Nullable String city,
                              @Nullable String country,
                              @Nullable String url,
                              @Nullable String stadium,
                              @Nullable String twitter,
                              @Nullable String type) {
        return new AutoValue_Team(code, name, fullname, city, country, url, stadium, twitter, type);
    }

    public boolean isEmpty() {
        return getName() == null;
    }
}
