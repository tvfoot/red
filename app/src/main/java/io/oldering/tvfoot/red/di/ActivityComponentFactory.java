package io.oldering.tvfoot.red.di;

import android.app.Activity;
import io.oldering.tvfoot.red.RedApp;
import io.oldering.tvfoot.red.di.component.ActivityComponent;
import io.oldering.tvfoot.red.di.module.ActivityModule;

public final class ActivityComponentFactory {
  private ActivityComponentFactory() {
    throw new RuntimeException("can't touch this");
  }

  public static ActivityComponent create(Activity activity) {
    return RedApp.get(activity).getComponent().plus(new ActivityModule(activity));
  }
}
