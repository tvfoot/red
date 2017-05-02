package io.oldering.tvfoot.red.app.domain.matches;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import io.oldering.tvfoot.red.app.common.Notification;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import java.lang.ref.WeakReference;

import static io.oldering.tvfoot.red.app.common.PreConditions.checkMainThread;

/**
 * Heavily based on {@link com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEventObservable}
 * and {@link com.genius.groupie.example.InfiniteScrollListener}
 */
final class InfiniteScrollEventObservable extends Observable<Object> {
  // TODO(benoit) is it useful to use a WeakReference here?
  private final WeakReference<RecyclerView> view;

  InfiniteScrollEventObservable(RecyclerView view) {
    this.view = new WeakReference<>(view);
  }

  @Override protected void subscribeActual(Observer<? super Object> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view.get(), observer);
    observer.onSubscribe(listener);
    view.get().addOnScrollListener(listener.scrollListener);
  }

  private static final class Listener extends MainThreadDisposable {
    private final RecyclerView recyclerView;
    private final RecyclerView.OnScrollListener scrollListener;

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true;
    // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 5;
    // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem, visibleItemCount, totalItemCount;

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
            observer.onNext(Notification.INSTANCE);
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