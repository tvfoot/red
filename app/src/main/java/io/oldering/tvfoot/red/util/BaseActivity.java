package io.oldering.tvfoot.red.util;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import io.oldering.tvfoot.red.di.ActivityComponentFactory;
import io.oldering.tvfoot.red.di.component.ActivityComponent;

public abstract class BaseActivity extends AppCompatActivity {
  protected ActivityComponent activityComponent;
  private BundleService bundleService;

  @CallSuper @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public ActivityComponent getActivityComponent() {
    if (activityComponent == null) {
      activityComponent = ActivityComponentFactory.create(this);
    }
    return activityComponent;
  }

  public BundleService getBundleService() {
    return bundleService;
  }
}