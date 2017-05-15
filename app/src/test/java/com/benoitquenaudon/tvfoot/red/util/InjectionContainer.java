package com.benoitquenaudon.tvfoot.red.util;

import com.benoitquenaudon.tvfoot.red.app.injection.component.TestComponent;

public final class InjectionContainer {
  private TestComponent testComponentInstance;

  public TestComponent testComponent() {
    if (testComponentInstance == null) {
      testComponentInstance = Dagger2Helper.buildComponent(TestComponent.class);
    }
    return testComponentInstance;
  }
}
