package io.oldering.tvfoot.red.util;

import android.support.v7.app.AppCompatActivity;
import io.oldering.tvfoot.red.di.ActivityComponentFactory;
import io.oldering.tvfoot.red.di.component.ActivityComponent;
import io.oldering.tvfoot.red.util.BundleService;

public abstract class BaseActivity extends AppCompatActivity {
  protected ActivityComponent activityComponent;
  private BundleService bundleService;

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
