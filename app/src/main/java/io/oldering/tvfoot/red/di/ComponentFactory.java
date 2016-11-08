package io.oldering.tvfoot.red.di;

public class ComponentFactory {
    public AppComponent buildComponent() {
        return componentBuilder().build();
    }

    DaggerAppComponent.Builder componentBuilder() {
        return DaggerAppComponent.builder();
    }
}
