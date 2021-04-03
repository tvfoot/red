package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.common.BaseActivity
import com.benoitquenaudon.tvfoot.red.databinding.ActivityMatchesBinding
import kotlin.LazyThreadSafetyMode.NONE

class MatchesActivity : BaseActivity() {
  private val binding: ActivityMatchesBinding by lazy(NONE) {
    DataBindingUtil.setContentView<ActivityMatchesBinding>(this, R.layout.activity_matches)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setupView()
  }

  private fun setupView() {
    setSupportActionBar(binding.matchesToolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)
  }
}
