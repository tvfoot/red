package io.oldering.tvfoot.red.app.domain.matches;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.app.common.BaseActivity;
import io.oldering.tvfoot.red.app.common.InfiniteScrollEventObservable;
import io.oldering.tvfoot.red.app.common.flowcontroller.FlowController;
import io.oldering.tvfoot.red.app.domain.matches.state.MatchesIntent;
import io.oldering.tvfoot.red.app.domain.matches.state.MatchesStateBinder;
import io.oldering.tvfoot.red.app.domain.matches.state.MatchesViewState;
import io.oldering.tvfoot.red.databinding.ActivityMatchesBinding;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static io.oldering.tvfoot.red.app.common.PreConditions.checkNotNull;

public class MatchesActivity extends BaseActivity {
  @Inject MatchesAdapter adapter;
  @Inject FlowController flowController;
  @Inject MatchesViewModel viewModel;
  @Inject MatchesStateBinder stateBinder;
  @Inject CompositeDisposable disposables;

  private ActivityMatchesBinding binding;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivityComponent().inject(this);

    setupView();

    disposables = new CompositeDisposable();
    bind();
  }

  private void setupView() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_matches);
    binding.recyclerView.setAdapter(adapter);
    binding.setViewModel(viewModel);

    setSupportActionBar(binding.matchesToolbar);
    ActionBar actionBar =
        checkNotNull(getSupportActionBar(), "support action bar should not be null");
    actionBar.setDisplayShowTitleEnabled(false);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.matches_settings_menu, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.matches_settings_item) {
      flowController.toSettings();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void bind() {
    disposables.add(stateBinder.getStatesAsObservable().subscribe(this::render));
    stateBinder.forwardIntents(intents());

    disposables.add(adapter.getMatchRowClickObservable()
        .subscribe(match -> flowController.toMatch(match.matchId())));
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    disposables.dispose();
  }

  public Observable<MatchesIntent> intents() {
    return Observable.merge(InitialIntent(), refreshIntent(), loadNextPageIntent());
  }

  private Observable<MatchesIntent.InitialIntent> InitialIntent() {
    return Observable.just(MatchesIntent.InitialIntent.create());
  }

  private Observable<MatchesIntent.RefreshIntent> refreshIntent() {
    return RxSwipeRefreshLayout.refreshes(binding.swipeRefreshLayout)
        .map(ignored -> MatchesIntent.RefreshIntent.create());
  }

  private Observable<MatchesIntent.LoadNextPageIntent> loadNextPageIntent() {
    return new InfiniteScrollEventObservable(binding.recyclerView).map(
        ignored -> MatchesIntent.LoadNextPageIntent.create(viewModel.getCurrentPage() + 1));
  }

  public void render(MatchesViewState state) {
    viewModel.updateFromState(state);
  }
}
