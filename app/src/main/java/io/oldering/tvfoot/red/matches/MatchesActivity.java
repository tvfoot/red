package io.oldering.tvfoot.red.matches;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.databinding.ActivityMatchesBinding;
import io.oldering.tvfoot.red.util.BaseActivity;
import io.oldering.tvfoot.red.util.InfiniteScrollListener;
import io.reactivex.Observable;
import java.util.List;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MatchesActivity extends BaseActivity {
  private ActivityMatchesBinding binding;
  private MatchesAdapter adapter;
  private LinearLayoutManager layoutManager;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    adapter = new MatchesAdapter();

    binding = DataBindingUtil.setContentView(this, R.layout.activity_matches);
    binding.recyclerView.setAdapter(adapter);
    layoutManager = (LinearLayoutManager) binding.recyclerView.getLayoutManager();
    binding.recyclerView.addOnScrollListener(new InfiniteScrollListener(layoutManager) {
      @Override public void onLoadMore(int current_page) {
        // TODO not needed anymore?
      }
    });

    new MatchesBinder(this, new MatchesRepository(getActivityComponent().matchService())).bind();
  }

  public Observable<MatchesIntent> matchRowClickIntent() {
    return adapter.getMatchRowClickObservable();
  }

  public Observable<MatchesIntent> loadFirstPageIntent() {
    return Observable.just(new MatchesIntent.LoadFirstPage());
  }

  public Observable<MatchesIntent> loadNextPageIntent() {
    return new InfiniteScrollEventObservable(binding.recyclerView).map(
        ignored -> new MatchesIntent.LoadNextPage());
  }

  public void render(MatchesViewState viewState) {
    switch (viewState.status()) {
      case FIRST_PAGE_LOADING:
        renderFirstPageLoading();
        break;
      case FIRST_PAGE_ERROR:
        renderFirstPageError(viewState.firstPageError());
        break;
      case NEXT_PAGE_LOADING:
        renderNextPageLoading();
        break;
      case NEXT_PAGE_ERROR:
        renderNextPageError(viewState.nextPageError());
        break;
      case FIRST_PAGE_LOADED:
      case NEXT_PAGE_LOADED:
        if (viewState.matches().isEmpty()) {
          renderEmptyResult();
        } else {
          renderResult(viewState.matches());
        }
        break;
      case PULL_TO_REFRESH_LOADING:
        break;
      case PULL_TO_REFRESH_ERROR:
        break;
      case PULL_TO_REFRESH_LOADED:
        break;
      case MATCH_ROW_CLICK:
        Timber.d("match row click %s", viewState.match());
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
    binding.loadingView.setVisibility(VISIBLE);
  }

  private void renderNextPageError(@Nullable Throwable throwable) {
    Timber.e(throwable, "First Page Error");
    renderError();
  }

  private void renderResult(List<MatchRowDisplayable> matches) {
    binding.emptyView.setVisibility(GONE);
    binding.errorView.setVisibility(GONE);
    binding.recyclerView.setVisibility(VISIBLE);
    binding.loadingView.setVisibility(GONE);
    adapter.setMatches(matches);
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
