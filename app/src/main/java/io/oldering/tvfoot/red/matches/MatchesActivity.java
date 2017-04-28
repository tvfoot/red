package io.oldering.tvfoot.red.matches;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.ActivityMatchesBinding;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import io.oldering.tvfoot.red.matches.state.MatchesIntent;
import io.oldering.tvfoot.red.matches.state.MatchesStateBinder;
import io.oldering.tvfoot.red.matches.state.MatchesViewState;
import io.oldering.tvfoot.red.util.BaseActivity;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import javax.annotation.Nullable;
import javax.inject.Inject;

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

    binding = DataBindingUtil.setContentView(this, R.layout.activity_matches);
    binding.recyclerView.setAdapter(adapter);
    binding.setViewModel(viewModel);

    disposables = new CompositeDisposable();
    bind();
  }

  //private void setStateBinder() {
  //  Object lastCustomNonConfigInstance = getLastCustomNonConfigurationInstance();
  //  if (lastCustomNonConfigInstance != null) {
  //    stateBinder = (MatchesStateBinder) lastCustomNonConfigInstance;
  //  } else {
  //    stateBinder = // TODO do I have to use old school Dagger Components?;
  //  }
  //}

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
    return Observable.merge(InitialIntent(), loadNextPageIntent());
  }

  private Observable<MatchesIntent.InitialIntent> InitialIntent() {
    return Observable.just(MatchesIntent.InitialIntent.create());
  }

  private Observable<MatchesIntent.LoadNextPageIntent> loadNextPageIntent() {
    return new InfiniteScrollEventObservable(binding.recyclerView).map(
        ignored -> MatchesIntent.LoadNextPageIntent.create(viewModel.getCurrentPage() + 1));
  }

  public void render(MatchesViewState state) {
    viewModel.updateFromState(state);
  }

  //@Override public Object onRetainCustomNonConfigurationInstance() {
  //  return stateBinder;
  //}
}
