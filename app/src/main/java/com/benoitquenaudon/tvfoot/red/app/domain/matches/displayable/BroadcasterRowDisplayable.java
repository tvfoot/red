package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class BroadcasterRowDisplayable implements Parcelable {
  public abstract String logoPath();

  public static BroadcasterRowDisplayable create(String broadcasterCode) {
    return new AutoValue_BroadcasterRowDisplayable(
        String.format("/images/broadcasters/%s_120.png", broadcasterCode));
  }
}
