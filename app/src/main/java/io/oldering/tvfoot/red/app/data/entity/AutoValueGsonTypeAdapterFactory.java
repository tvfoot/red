package io.oldering.tvfoot.red.app.data.entity;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory public abstract class AutoValueGsonTypeAdapterFactory
    implements TypeAdapterFactory {
  public static TypeAdapterFactory create() {
    return new AutoValueGson_AutoValueGsonTypeAdapterFactory();
  }
}