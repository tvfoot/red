package com.benoitquenaudon.tvfoot.red.app.domain.main;

import android.os.Bundle;
import com.benoitquenaudon.tvfoot.red.app.common.BaseActivity;
import com.benoitquenaudon.tvfoot.red.app.common.flowcontroller.FlowController;
import javax.annotation.Nullable;
import javax.inject.Inject;

public class MainActivity extends BaseActivity {
  @Inject FlowController flowController;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivityComponent().inject(this);

    flowController.toMatches();
    finish();
  }
}