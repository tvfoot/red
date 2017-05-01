package io.oldering.tvfoot.red.util;

import io.oldering.tvfoot.red.app.injection.component.TestComponent;

public final class InjectionContainer {
  private TestComponent testComponentInstance;

  public TestComponent testComponent() {
    if (testComponentInstance == null) {
      testComponentInstance = Dagger2Helper.buildComponent(TestComponent.class);
    }
    return testComponentInstance;
  }
}
