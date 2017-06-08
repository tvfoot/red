package com.benoitquenaudon.tvfoot.red.app.common;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.benoitquenaudon.tvfoot.red.app.common.PreConditions.checkMainThread;

/**
 * Heavily based on {@link com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEventObservable}
 * and {@link com.genius.groupie.example.InfiniteScrollListener}
 */
public final class InfiniteScrollEventObservable extends Observable<Object> {
  private final RecyclerView view;

  public InfiniteScrollEventObservable(RecyclerView view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Object> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnScrollListener(listener.scrollListener);
  }

  private static final class Listener extends MainThreadDisposable {
    private final RecyclerView recyclerView;
    final RecyclerView.OnScrollListener scrollListener;

    int previousTotal = 0; // The total number of items in the dataset after the last load
    boolean loading = true;
    // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 5;
    // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    Listener(RecyclerView recyclerView, final Observer<? super Object> observer) {
      this.recyclerView = recyclerView;
      this.scrollListener = new RecyclerView.OnScrollListener() {
        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          if (isDisposed()) return;

          LinearLayoutManager linearLayoutManager =
              (LinearLayoutManager) recyclerView.getLayoutManager();
          visibleItemCount = recyclerView.getChildCount();
          totalItemCount = linearLayoutManager.getItemCount();
          firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

          if (totalItemCount < previousTotal) {
            previousTotal = totalItemCount;
          }

          if (loading && (totalItemCount > previousTotal || totalItemCount == 0)) {
            loading = false;
            previousTotal = totalItemCount;
          }

          // End has been reached
          if (!loading && //
              (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            observer.onNext(StreamNotification.INSTANCE);
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