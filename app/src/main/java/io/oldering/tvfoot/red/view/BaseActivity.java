package io.oldering.tvfoot.red.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import io.oldering.tvfoot.red.di.ActivityComponentFactory;
import io.oldering.tvfoot.red.di.component.ActivityComponent;
import io.oldering.tvfoot.red.util.BundleService;


public abstract class BaseActivity extends Activity {
    protected ActivityComponent activityComponent;
    private BundleService bundleService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO(benoit) need to refact this for dataBinding
        setContentView(getLayout());
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

    protected abstract int getLayout();
}
