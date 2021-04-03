package com.benoitquenaudon.tvfoot.red.app.domain.main

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import com.benoitquenaudon.tvfoot.red.app.common.BaseActivity

class MainActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    startActivity(Intent(Intent.ACTION_VIEW, "tvfoot://tvfoot/".toUri()))
    finish()
  }
}