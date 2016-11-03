package io.oldering.tvfoot.red.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.ActivityMatchDetailBinding;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import io.oldering.tvfoot.red.viewmodel.MatchViewModel;


public class MatchDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMatchDetailBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_match_detail);

        Intent intent = getIntent();
        MatchViewModel matchVM = intent.getParcelableExtra(FlowController.MATCH_VIEW_MODEL);
        if (matchVM != null) {
            dataBinding.setMatch(matchVM);

            RecyclerView recyclerView = dataBinding.matchDetailBroadcasters;
            // set horizontal linearlayout
            // set adapter
        } else {
            throw new RuntimeException("Where is my match bro");
        }
    }
}
