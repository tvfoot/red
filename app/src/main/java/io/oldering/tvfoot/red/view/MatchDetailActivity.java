package io.oldering.tvfoot.red.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.ActivityMatchDetailBinding;

public class MatchDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMatchDetailBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_match_detail);
    }
}
