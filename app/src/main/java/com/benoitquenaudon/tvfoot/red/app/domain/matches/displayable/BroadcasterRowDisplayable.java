package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class BroadcasterRowDisplayable implements Parcelable {
  public abstract String name();

  public abstract String logoPath();

  public static BroadcasterRowDisplayable create(String name, String broadcasterCode) {
    return new AutoValue_BroadcasterRowDisplayable(name,
        String.format("/images/broadcasters/%s_120.png", broadcasterCode));
  }
}
