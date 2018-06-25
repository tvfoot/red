package com.benoitquenaudon.tvfoot.red.app.common

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.api.TvfootService
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target

@BindingAdapter("tvFootTeamLogoPathLeftDrawable")
fun setTvFootTeamLogoToLeftDrawable(textView: TextView, logoPath: String?) {
  if (logoPath == null) return

  val mediumLogoPath = logoPath.replace("/large/", "/medium/")

  Picasso.with(textView.context)
      .load(Uri.parse(TvfootService.BASE_URL + mediumLogoPath))
      .into(object : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        }

        override fun onBitmapFailed(errorDrawable: Drawable?) {
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
          if (bitmap == null) return

          textView.setCompoundDrawablesWithIntrinsicBounds(
              BitmapDrawable(textView.context.resources, bitmap), null, null, null
          )
        }
      })
}

@BindingAdapter("tvfootTeamMediumLogoPath")
fun setTvFootTeamMediumLogo(imageView: ImageView, logoPath: String?) {
  if (logoPath == null) {
    // in_flight, errors etc.
    return
  }

  val mediumLogoPath = logoPath
      .replace("/large/", "/medium/")
      .replace(".png", "@2x.png")

  loadTvFootImage(
      imageView = imageView,
      path = mediumLogoPath,
      placeholderResId = R.drawable.default_team_logo)
}

@BindingAdapter("tvfootTeamLogoPath")
fun setTvFootTeamLogo(imageView: ImageView, logoPath: String?) {
  if (logoPath == null) {
    // in_flight, errors etc.
    return
  }

  loadTvFootImage(
      imageView = imageView,
      path = logoPath,
      placeholderResId = R.drawable.default_team_logo)
}

@BindingAdapter("tvfootBroadcasterLogoPath")
fun setTvFootBroadcasterLogo(imageView: ImageView, logoPath: String?) {
  if (logoPath == null) {
    // in_flight, errors etc.
    return
  }

  loadTvFootImage(
      imageView = imageView,
      path = logoPath,
      placeholderResId = R.drawable.ic_tv_black_24px)
}

private fun loadTvFootImage(imageView: ImageView, path: String, placeholderResId: Int) {
  Picasso.with(imageView.context)
      .load(Uri.parse(TvfootService.BASE_URL + path))
      .fit().centerInside()
      .placeholder(placeholderResId)
      .into(imageView)
}

@BindingAdapter("visible")
fun setVisibility(view: View, isVisible: Boolean) {
  view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("dangerText")
fun setDangerText(textView: TextView, textResId: Int) {
  if (textResId < 1) return

  textView.setText(textResId)
}
