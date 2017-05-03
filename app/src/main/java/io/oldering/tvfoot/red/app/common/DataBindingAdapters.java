package io.oldering.tvfoot.red.app.common;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.RedApp;
import javax.annotation.Nullable;

import static io.oldering.tvfoot.red.api.TvfootService.BASE_URL;

public class DataBindingAdapters {

  @BindingAdapter("imageResource")
  public static void setImageResource(ImageView imageView, String resource) {
    Context context = imageView.getContext();
    int identifier =
        context.getResources().getIdentifier(resource, "drawable", context.getPackageName());
    imageView.setImageResource(identifier);
  }

  @BindingAdapter("tvfootTeamImageResource")
  public static void setTvFootTeamImageResource(ImageView imageView, @Nullable String logoPath) {
    if (logoPath == null) {
      // in_flight, errors etc.
      return;
    }

    RedApp.get(imageView.getContext())
        .getComponent()
        .picasso()
        .load(Uri.parse(BASE_URL + logoPath))
        .placeholder(R.drawable.default_team_logo)
        .into(imageView);
  }

  @BindingAdapter("visible") public static void setVisibility(View view, boolean isVisible) {
    view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
  }
}