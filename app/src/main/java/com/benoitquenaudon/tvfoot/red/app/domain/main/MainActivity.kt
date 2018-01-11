package com.benoitquenaudon.tvfoot.red.app.domain.main

import android.os.Bundle
import com.benoitquenaudon.tvfoot.red.app.common.BaseActivity
import com.benoitquenaudon.tvfoot.red.app.common.flowcontroller.FlowController
import javax.inject.Inject

class MainActivity : BaseActivity() {
  @Inject lateinit var flowController: FlowController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    flowController.toMatches()
    finish()
  }
}