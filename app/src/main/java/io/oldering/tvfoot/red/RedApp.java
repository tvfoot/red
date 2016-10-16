package io.oldering.tvfoot.red;

import android.app.Application;

import io.oldering.tvfoot.red.di.AppComponent;
import io.oldering.tvfoot.red.di.DaggerAppComponent;

public class RedApp extends Application {
    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.appComponent = DaggerAppComponent.create();
    }

    public AppComponent getAppComponent() {
        return this.appComponent;
    }
}
