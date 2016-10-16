package io.oldering.tvfoot.red.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.oldering.tvfoot.red.flowcontroller.FlowController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlowController.launchMatchListActivity(this);
        finish();
    }
}