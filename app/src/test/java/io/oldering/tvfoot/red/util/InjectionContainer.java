package io.oldering.tvfoot.red.util;

import io.oldering.tvfoot.red.data.api.MatchService;
import io.oldering.tvfoot.red.di.component.TestComponent;
import io.oldering.tvfoot.red.di.module.NetworkModule;

public final class InjectionContainer {
  private TestComponent testComponentInstance;

  public TestComponent testComponent() {
    if (testComponentInstance == null) {
      testComponentInstance = Dagger2Helper.buildComponent(TestComponent.class,
          new NetworkModule(MatchService.BASE_URL));
    }
    return testComponentInstance;
  }
}
