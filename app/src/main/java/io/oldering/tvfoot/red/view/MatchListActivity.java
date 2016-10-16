package io.oldering.tvfoot.red.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.genius.groupie.GroupAdapter;
import com.genius.groupie.Item;

import javax.inject.Inject;

import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.ActivityMatchListBinding;
import io.oldering.tvfoot.red.di.DaggerAppComponent;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.oldering.tvfoot.red.view.item.DayHeaderItem;
import io.oldering.tvfoot.red.viewmodel.MatchListViewModel;
import io.reactivex.disposables.CompositeDisposable;

public class MatchListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MatchListActivity";

    private GroupAdapter matchListGroupAdapter;
    @Inject
    MatchListViewModel matchListVM;
    @Inject
    BaseSchedulerProvider schedulerProvider;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerAppComponent.create().inject(this);

        ActivityMatchListBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_match_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.match_list_filter_toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = dataBinding.matchListRecyclerView;
        matchListGroupAdapter = new GroupAdapter(this);
        recyclerView.setAdapter(matchListGroupAdapter);
        recyclerView.addOnScrollListener(new InfiniteScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d(TAG, "onLoadMore: " + current_page);
                matchListVM.getMore();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.match_list_filter_fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        bind();

        matchListVM.getMatches();
    }

    private void bind() {
        disposables.add(matchListVM
                .matchStream()
                .observeOn(schedulerProvider.ui())
                .subscribe(this::addItem));
    }

    @Override
    protected void onStop() {
        super.onResume();
        unbind();
    }

    private void unbind() {
        disposables.clear();
    }

    private void addItem(Item item) {
        if (item instanceof DayHeaderItem) {
            Log.d(TAG, "addItem: is day header" + ((DayHeaderItem) item).dayHeaderVM.getDisplayedDate());
        }
        matchListGroupAdapter.add(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
    }
}
