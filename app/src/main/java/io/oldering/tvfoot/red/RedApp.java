package io.oldering.tvfoot.red;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import io.oldering.tvfoot.red.di.AppComponent;
import io.oldering.tvfoot.red.di.DaggerAppComponent;
import timber.log.Timber;

public class RedApp extends Application {
    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.appComponent = DaggerAppComponent.create();

        setupTimber();
        setupLeakCanary();
    }

    public AppComponent getAppComponent() {
        return this.appComponent;
    }

    private void setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
