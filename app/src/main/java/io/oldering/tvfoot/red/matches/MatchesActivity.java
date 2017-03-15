package io.oldering.tvfoot.red.matches;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.data.model.Match;
import io.oldering.tvfoot.red.databinding.ActivityMatchesBinding;
import io.oldering.tvfoot.red.util.BaseActivity;
import io.oldering.tvfoot.red.util.InfiniteScrollListener;
import io.reactivex.Observable;
import java.util.List;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MatchesActivity extends BaseActivity implements MatchesView {
  private ActivityMatchesBinding binding;
  private MatchesAdapter adapter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    adapter = new MatchesAdapter();

    binding = DataBindingUtil.setContentView(this, R.layout.activity_matches);
    binding.recyclerView.setAdapter(adapter);
    binding.recyclerView.addOnScrollListener(
        new InfiniteScrollListener((LinearLayoutManager) binding.recyclerView.getLayoutManager()) {
          @Override public void onLoadMore(int current_page) {
            // TODO not needed anymore?
          }
        });

    MatchesPresenter presenter =
        new MatchesPresenter(this, new MatchesInteractor(getActivityComponent().matchService()));
    presenter.bindIntents();
  }

  @Override public Observable<Boolean> loadFirstPageIntent() {
    Timber.d("loadFirstPageIntent");
    return Observable.just(true).doOnComplete(() -> Timber.d("firstPage completed"));
  }

  @Override public Observable<Boolean> loadNextPageIntent() {
    return RxRecyclerView.scrollStateChanges(binding.recyclerView)
        .filter(event -> event == RecyclerView.SCROLL_STATE_IDLE)
        .filter(
            event -> ((LinearLayoutManager) binding.recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition()
                == adapter.getItemCount() - 1)
        .map(integer -> true);
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
    binding.recyclerView.setVisibility(GONE);
    binding.loadingView.setVisibility(VISIBLE);
  }

  private void renderNextPageError(@Nullable Throwable throwable) {
    Timber.e(throwable, "First Page Error");
    renderError();
  }

  private void renderResult(List<Match> matches) {
    binding.emptyView.setVisibility(GONE);
    binding.errorView.setVisibility(GONE);
    binding.recyclerView.setVisibility(VISIBLE);
    binding.loadingView.setVisibility(GONE);
    adapter.setMatches(matches);
    adapter.notifyDataSetChanged();
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
