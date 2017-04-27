package io.oldering.tvfoot.red.di;

import android.app.Activity;
import io.oldering.tvfoot.red.RedApp;
import io.oldering.tvfoot.red.di.component.ActivityComponent;
import io.oldering.tvfoot.red.di.component.ScreenComponent;
import io.oldering.tvfoot.red.di.module.ActivityModule;

public final class ComponentFactory {
  private ComponentFactory() {
    throw new RuntimeException("can't touch this");
  }

  public static ScreenComponent screenComponent(Activity activity) {
    return RedApp.get(activity).getComponent().screenComponent();
  }

  public static ActivityComponent activityComponent(ScreenComponent screenComponent,
      Activity activity) {
    return screenComponent.plus(new ActivityModule(activity));
  }
}