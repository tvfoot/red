package io.oldering.tvfoot.red.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.genius.groupie.GroupAdapter;

import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.ActivityMatchDetailBinding;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import io.oldering.tvfoot.red.view.item.BroadcasterItem;
import io.oldering.tvfoot.red.viewmodel.BroadcasterViewModel;
import io.oldering.tvfoot.red.viewmodel.MatchViewModel;


public class MatchDetailActivity extends AppCompatActivity {


    private MatchViewModel matchVM;
    private ActivityMatchDetailBinding dataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_match_detail);

        Intent intent = getIntent();
        matchVM = intent.getParcelableExtra(FlowController.MATCH_VIEW_MODEL);
        if (matchVM == null) {
            throw new RuntimeException("Where is my match bro");
        }

        setupBindings();
        setupViews();
    }

    private void setupBindings() {
        dataBinding.setMatch(matchVM);
    }

    private void setupViews() {
        GroupAdapter broadcastersAdapter = new GroupAdapter();
        for (BroadcasterViewModel broadcasterViewModel : matchVM.getBroadcasters()) {
            broadcastersAdapter.add(new BroadcasterItem(broadcasterViewModel.getCode()));
        }
        dataBinding.matchDetailBroadcasters.setAdapter(broadcastersAdapter);
    }
}
