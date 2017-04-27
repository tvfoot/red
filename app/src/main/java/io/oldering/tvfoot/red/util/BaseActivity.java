package io.oldering.tvfoot.red.util;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import io.oldering.tvfoot.red.di.ComponentFactory;
import io.oldering.tvfoot.red.di.component.ActivityComponent;
import io.oldering.tvfoot.red.di.component.ScreenComponent;

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