package io.oldering.tvfoot.red.matches;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.oldering.tvfoot.red.util.Preconditions.checkMainThread;

public final class InfiniteScrollEventObservable extends Observable<Integer> {
  private final RecyclerView view;

  InfiniteScrollEventObservable(RecyclerView view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Integer> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnScrollListener(listener.scrollListener);
  }

  final class Listener extends MainThreadDisposable {
    private final RecyclerView recyclerView;
    private final RecyclerView.OnScrollListener scrollListener;

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true;
    // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 5;
    // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int current_page = 0;

    Listener(RecyclerView recyclerView, final Observer<? super Integer> observer) {
      this.recyclerView = recyclerView;
      this.scrollListener = new RecyclerView.OnScrollListener() {
        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          if (isDisposed()) return;

          LinearLayoutManager linearLayoutManager =
              (LinearLayoutManager) recyclerView.getLayoutManager();
          visibleItemCount = recyclerView.getChildCount();
          totalItemCount = linearLayoutManager.getItemCount();
          firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

          if (loading) {
            if (totalItemCount > previousTotal || totalItemCount == 0) {
              loading = false;
              previousTotal = totalItemCount;
            }
          }

          // End has been reached
          if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem
              + visibleThreshold)) {
            current_page++;
            observer.onNext(current_page);
            loading = true;
          }
        }
      };
    }

    @Override protected void onDispose() {
      recyclerView.removeOnScrollListener(scrollListener);
    }
  }
}