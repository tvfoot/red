package com.benoitquenaudon.tvfoot.red.util

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.DITHER_FLAG
import android.graphics.Paint.FILTER_BITMAP_FLAG
import android.graphics.Shader.TileMode.CLAMP
import androidx.core.graphics.createBitmap
import com.squareup.picasso.Transformation

object CircleTransform : Transformation {
  override fun transform(source: Bitmap): Bitmap {
    val size = Math.min(source.width, source.height)
    val x = (source.width - size) / 2
    val y = (source.height - size) / 2

    val squared = Bitmap.createBitmap(source, x, y, size, size)
    val result = createBitmap(size, size)

    val canvas = Canvas(result)
    val paint = Paint(FILTER_BITMAP_FLAG or DITHER_FLAG or ANTI_ALIAS_FLAG)
    paint.shader = BitmapShader(squared, CLAMP, CLAMP)

    val r = size / 2f
    canvas.drawCircle(r, r, r, paint)

    if (source != result) {
      source.recycle()
    }

    return result
  }

  override fun key(): String = javaClass.name
}