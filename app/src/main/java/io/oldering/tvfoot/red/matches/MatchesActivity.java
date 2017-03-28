package io.oldering.tvfoot.red.matches;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import dagger.android.AndroidInjection;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.ActivityMatchesBinding;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.MatchesItemDisplayable;
import io.reactivex.Observable;
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

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);

    binding = DataBindingUtil.setContentView(this, R.layout.activity_matches);
    TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot());
    binding.recyclerView.setAdapter(adapter);

    binder.bind();
  }

  public Observable<MatchesIntent> matchRowClickIntent() {
    return adapter.getMatchRowClickObservable();
  }

  public Observable<MatchesIntent> loadFirstPageIntent() {
    return Observable.just(MatchesIntent.LoadFirstPage.create());
  }

  public Observable<MatchesIntent> loadNextPageIntent() {
    return new InfiniteScrollEventObservable(binding.recyclerView).map(
        MatchesIntent.LoadNextPage::create);
  }

  public void render(MatchesViewState state) {
    switch (state.status()) {
      case FIRST_PAGE_LOADING:
        renderFirstPageLoading();
        break;
      case FIRST_PAGE_ERROR:
        renderFirstPageError(state.firstPageError());
        break;
      case NEXT_PAGE_LOADING:
        renderNextPageLoading();
        break;
      case NEXT_PAGE_ERROR:
        renderNextPageError(state.nextPageError());
        break;
      case FIRST_PAGE_LOADED:
      case NEXT_PAGE_LOADED:
        if (state.matchesItemDisplayables().isEmpty()) {
          renderEmptyResult();
        } else {
          renderResult(state.matchesItemDisplayables());
        }
        break;
      case PULL_TO_REFRESH_LOADING:
        break;
      case PULL_TO_REFRESH_ERROR:
        break;
      case PULL_TO_REFRESH_LOADED:
        break;
      case MATCH_ROW_CLICK:
        MatchRowDisplayable match = checkNotNull(state.match(), "MatchRowClick's match == null");
        flowController.toMatch(match.matchId(),
            checkNotNull(state.headlineView(), "headline is null"));
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
    Timber.e(throwable, "First Page Error");
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
