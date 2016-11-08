package io.oldering.tvfoot.red.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import io.oldering.tvfoot.red.util.BundleService;


public class BaseActivity extends Activity {

    private BundleService bundleService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundleService = new BundleService(savedInstanceState, getIntent().getExtras());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(bundleService.getAll());
    }

    public BundleService getBundleService() {
        return bundleService;
    }
}
