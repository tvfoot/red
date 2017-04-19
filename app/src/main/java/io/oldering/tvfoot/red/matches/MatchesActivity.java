package io.oldering.tvfoot.red.matches;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import dagger.android.AndroidInjection;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.ActivityMatchesBinding;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.MatchesItemDisplayable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static io.oldering.tvfoot.red.util.Preconditions.checkNotNull;

public class MatchesActivity extends AppCompatActivity {
  private ActivityMatchesBinding binding;
  @Inject MatchesAdapter adapter;
  @Inject MatchesBinder binder;
  @Inject FlowController flowController;
  CompositeDisposable disposables;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);

    binding = DataBindingUtil.setContentView(this, R.layout.activity_matches);
    binding.recyclerView.setAdapter(adapter);

    disposables = new CompositeDisposable();
    bind();
  }

  @Override protected void onStart() {
    super.onStart();
  }

  private void bind() {
    disposables.add(binder.compose(intents()).subscribe(this::render));
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    disposables.dispose();
  }

  @Override protected void onStop() {
    super.onStop();
  }

  public Observable<MatchesIntent> intents() {
    return Observable.merge(loadFirstPageIntent(), loadNextPageIntent(), matchRowClickIntent());
  }

  public Observable<MatchesIntent.MatchRowClickIntent> matchRowClickIntent() {
    return adapter.getMatchRowClickObservable();
  }

  public Observable<MatchesIntent.LoadFirstPageIntent> loadFirstPageIntent() {
    return Observable.just(MatchesIntent.LoadFirstPageIntent.create());
  }

  public Observable<MatchesIntent.LoadNextPageIntent> loadNextPageIntent() {
    return new InfiniteScrollEventObservable(binding.recyclerView).map(
        MatchesIntent.LoadNextPageIntent::create);
  }

  public void render(MatchesViewState state) {
    switch (state.status()) {
      case FIRST_PAGE_IN_FLIGHT:
        renderFirstPageLoading();
        break;
      case FIRST_PAGE_FAILURE:
        renderFirstPageError(state.firstPageError());
        break;
      case NEXT_PAGE_IN_FLIGHT:
        renderNextPageLoading();
        break;
      case NEXT_PAGE_FAILURE:
        renderNextPageError(state.nextPageError());
        break;
      case FIRST_PAGE_SUCCESS:
      case NEXT_PAGE_SUCCESS:
        if (state.matches().isEmpty()) {
          renderEmptyResult();
        } else {
          renderResult(state.matchesItemDisplayables());
        }
        break;
      case MATCH_ROW_CLICK:
        MatchRowDisplayable match =
            checkNotNull(state.match(), "MatchRowClickIntent's match == null");
        flowController.toMatch(match.matchId());
        break;
    }
  }

  private void renderFirstPageLoading() {
    binding.emptyView.setVisibility(GONE);
    binding.errorView.setVisibility(GONE);
    binding.recyclerView.setVisibility(GONE);
    binding.loadingView.setVisibility(VISIBLE);
  }

  private void renderFirstPageError(@Nullable Throwable throwable) {
    Timber.e(throwable, "First Page Error");
    renderError();
  }

  private void renderNextPageLoading() {
    binding.emptyView.setVisibility(GONE);
    binding.errorView.setVisibility(GONE);
    binding.recyclerView.setVisibility(VISIBLE);
    binding.loadingView.setVisibility(GONE);
  }

  private void renderNextPageError(@Nullable Throwable throwable) {
    Timber.e(throwable, "Next Page Error");
    renderError();
  }

  private void renderResult(List<MatchesItemDisplayable> matchesItemDisplayables) {
    adapter.setMatchesItems(matchesItemDisplayables);
    binding.emptyView.setVisibility(GONE);
    binding.errorView.setVisibility(GONE);
    binding.recyclerView.setVisibility(VISIBLE);
    binding.loadingView.setVisibility(GONE);
  }

  private void renderEmptyResult() {
    binding.emptyView.setVisibility(VISIBLE);
    binding.errorView.setVisibility(GONE);
    binding.recyclerView.setVisibility(GONE);
    binding.loadingView.setVisibility(GONE);
  }

  private void renderError() {
    binding.emptyView.setVisibility(GONE);
    binding.errorView.setVisibility(VISIBLE);
    binding.recyclerView.setVisibility(GONE);
    binding.loadingView.setVisibility(GONE);
  }
}
