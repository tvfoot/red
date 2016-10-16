package io.oldering.tvfoot.red.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class Team {
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

    public static Team create(String code, String name, String fullname, String city, String country, String url, String stadium, String twitter, String type) {
        return new AutoValue_Team(code, name, fullname, city, country, url, stadium, twitter, type);
    }

    public boolean isEmpty() {
        return getName() == null;
    }
}
