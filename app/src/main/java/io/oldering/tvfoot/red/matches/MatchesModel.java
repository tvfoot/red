package io.oldering.tvfoot.red.matches;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MatchesModel {
  private final MatchesActivity view;
  private final MatchesRepository repository;

  public MatchesModel(MatchesActivity view, MatchesRepository repository) {
    this.view = view;
    this.repository = repository;
  }

  void bindIntents() {
    Timber.d("binding intents");
    Observable<MatchesViewState> allIntents =
        Observable.merge(loadFirstPageModel(view.loadFirstPageIntent()),
            loadNextPageModel(view.loadNextPageIntent()));

    modelAll(allIntents).doOnNext(ignored -> Timber.d("onnext yo"))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(view::render);
  }

  Observable<MatchesViewState> modelAll(Observable<MatchesViewState> allIntents) {
    return allIntents.scan(MatchesViewState::reduce);
  }

  Observable<MatchesViewState> loadFirstPageModel(Observable<Boolean> loadFirstPageIntent) {
    return loadFirstPageIntent.doOnNext(ignored -> Timber.d("intent: load first page"))
        .flatMap(ignored -> repository.loadFirstPage().subscribeOn(Schedulers.io()));
  }

  Observable<MatchesViewState> loadNextPageModel(Observable<Boolean> loadNextPageIntent) {
    return loadNextPageIntent.doOnNext(ignored -> Timber.d("intent: load next page"))
        .flatMap(ignored -> repository.loadNextPage().subscribeOn(Schedulers.io()));
  }
}
