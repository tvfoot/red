package com.benoitquenaudon.tvfoot.red.app.common;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.benoitquenaudon.tvfoot.red.app.injection.ComponentFactory;
import com.benoitquenaudon.tvfoot.red.app.injection.component.ActivityComponent;
import com.benoitquenaudon.tvfoot.red.app.injection.component.ScreenComponent;
import com.benoitquenaudon.tvfoot.red.util.BundleService;

public abstract class BaseActivity extends AppCompatActivity {
  private ScreenComponent screenComponent;
  private ActivityComponent activityComponent;
  private BundleService bundleService;

  @CallSuper @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Object lastCustomNonConfigInstance = getLastCustomNonConfigurationInstance();
    if (lastCustomNonConfigInstance != null) {
      screenComponent = (ScreenComponent) lastCustomNonConfigInstance;
    }
  }

  public ScreenComponent getScreenComponent() {
    if (screenComponent == null) {
      screenComponent = ComponentFactory.screenComponent(this);
    }
    return screenComponent;
  }

  public ActivityComponent getActivityComponent() {
    if (activityComponent == null) {
      activityComponent = ComponentFactory.activityComponent(getScreenComponent(), this);
    }
    return activityComponent;
  }

  public BundleService getBundleService() {
    return bundleService;
  }

  @Override public Object onRetainCustomNonConfigurationInstance() {
    return getScreenComponent();
  }
}