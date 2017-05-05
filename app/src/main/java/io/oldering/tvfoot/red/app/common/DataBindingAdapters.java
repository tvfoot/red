package io.oldering.tvfoot.red.app.common;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.RedApp;
import javax.annotation.Nullable;

import static io.oldering.tvfoot.red.api.TvfootService.BASE_URL;

public class DataBindingAdapters {
  @BindingAdapter("tvfootTeamLogoPath")
  public static void setTvFootTeamLogo(ImageView imageView, @Nullable String logoPath) {
    if (logoPath == null) {
      // in_flight, errors etc.
      return;
    }

    loadTvFootImage(imageView, logoPath, R.drawable.default_team_logo);
  }

  @BindingAdapter("tvfootBroadcasterLogoPath")
  public static void setTvFootBroadcasterLogo(ImageView imageView, @Nullable String logoPath) {
    if (logoPath == null) {
      // in_flight, errors etc.
      return;
    }

    loadTvFootImage(imageView, logoPath, R.drawable.ic_tv_black_18px);
  }

  private static void loadTvFootImage(ImageView imageView, String path, int placeholderResId) {
    RedApp.get(imageView.getContext())
        .getComponent()
        .picasso()
        .load(Uri.parse(BASE_URL + path))
        .placeholder(placeholderResId)
        .into(imageView);
  }

  @BindingAdapter("visible") public static void setVisibility(View view, boolean isVisible) {
    view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
  }
}