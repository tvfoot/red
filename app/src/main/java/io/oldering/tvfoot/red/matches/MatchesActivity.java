package io.oldering.tvfoot.red.matches;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import dagger.android.AndroidInjection;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.ActivityMatchesBinding;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import io.oldering.tvfoot.red.matches.state.MatchesIntent;
import io.oldering.tvfoot.red.matches.state.MatchesStateBinder;
import io.oldering.tvfoot.red.matches.state.MatchesViewState;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static io.oldering.tvfoot.red.util.Preconditions.checkNotNull;

public class MatchesActivity extends AppCompatActivity {
  private ActivityMatchesBinding binding;
  @Inject MatchesAdapter adapter;
  @Inject FlowController flowController;
  @Inject MatchesViewModel viewModel;
  @Inject MatchesStateBinder stateBinder;
  CompositeDisposable disposables;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);

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
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    disposables.dispose();
  }

  public Observable<MatchesIntent> intents() {
    return Observable.merge(InitialIntent(), loadNextPageIntent(), matchRowClickIntent());
  }

  private Observable<MatchesIntent.MatchRowClickIntent> matchRowClickIntent() {
    return adapter.getMatchRowClickObservable();
  }

  private Observable<MatchesIntent.InitialIntent> InitialIntent() {
    return Observable.just(MatchesIntent.InitialIntent.create());
  }

  private Observable<MatchesIntent.LoadNextPageIntent> loadNextPageIntent() {
    return new InfiniteScrollEventObservable(binding.recyclerView).map(
        ignored -> MatchesIntent.LoadNextPageIntent.create(viewModel.getCurrentPage() + 1));
  }

  public void render(MatchesViewState state) {
    switch (state.status()) {
      case MATCH_ROW_CLICK:
        MatchRowDisplayable match =
            checkNotNull(state.match(), "MatchRowClickIntent's match == null");
        flowController.toMatch(match.matchId());
        break;
      default:
        viewModel.updateFromState(state);
    }
  }

  //@Override public Object onRetainCustomNonConfigurationInstance() {
  //  return stateBinder;
  //}
}
