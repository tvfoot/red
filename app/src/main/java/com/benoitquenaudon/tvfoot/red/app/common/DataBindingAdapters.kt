package com.benoitquenaudon.tvfoot.red.app.common

import android.databinding.BindingAdapter
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.RedApp
import com.benoitquenaudon.tvfoot.red.api.TvfootService


object DataBindingAdapters {
  @JvmStatic @BindingAdapter("tvfootTeamLogoPath")
  fun setTvFootTeamLogo(imageView: ImageView, logoPath: String?) {
    if (logoPath == null) {
      // in_flight, errors etc.
      return
    }

    loadTvFootImage(imageView, logoPath, R.drawable.default_team_logo)
  }

  @JvmStatic @BindingAdapter("tvfootBroadcasterLogoPath")
  fun setTvFootBroadcasterLogo(imageView: ImageView, logoPath: String?) {
    if (logoPath == null) {
      // in_flight, errors etc.
      return
    }

    loadTvFootImage(imageView, logoPath, R.drawable.ic_tv_black_18px)
  }

  private fun loadTvFootImage(imageView: ImageView, path: String, placeholderResId: Int) {
    RedApp.get(imageView.context)
        .component
        .picasso()
        .load(Uri.parse(TvfootService.BASE_URL + path))
        .fit().centerInside()
        .placeholder(placeholderResId)
        .into(imageView)
  }

  @JvmStatic @BindingAdapter("visible") fun setVisibility(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
  }

  @JvmStatic @BindingAdapter("dangerText") fun setDangerText(textView: TextView, textResId: Int) {
    if (textResId < 1) return

    textView.setText(textResId)
  }
}