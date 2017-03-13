package io.oldering.tvfoot.red.viewmodel;

import com.genius.groupie.Item;
import io.oldering.tvfoot.red.api.MatchService;
import io.oldering.tvfoot.red.model.Match;
import io.oldering.tvfoot.red.model.search.Filter;
import io.oldering.tvfoot.red.util.rxbus.RxBus;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.oldering.tvfoot.red.view.item.DayHeaderItem;
import io.oldering.tvfoot.red.view.item.MatchItem;
import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import javax.inject.Inject;

// TODO(benoit) delete and only use MatchViewModel / WHY ?
public class MatchListViewModel {
  public static SimpleDateFormat simpleDateFormat =
      new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
  private final MatchService matchService;
  private final BaseSchedulerProvider schedulerProvider;
  private final RxBus rxBus;
  private final int MATCH_PER_PAGE = 30;

  @Inject
  public MatchListViewModel(MatchService matchService, BaseSchedulerProvider schedulerProvider,
      RxBus rxBus) {
    this.matchService = matchService;
    this.schedulerProvider = schedulerProvider;
    this.rxBus = rxBus;

    simpleDateFormat.setTimeZone(TimeZone.getDefault());
  }

  public Observable<MatchItem> getMatches(Filter filter) {
    return findFuture(filter).map(match -> new MatchItem(MatchViewModel.create(match, rxBus)))
        .subscribeOn(schedulerProvider.io());
  }

  public Observable<Match> findFuture(Filter filter) {
    return matchService.findFuture(filter).toObservable().flatMap(Observable::fromIterable);
  }

  public Observable<GroupedObservable<String, Item>> groupByDate(Observable<Match> matches) {
    return matches.groupBy(match -> simpleDateFormat.format(match.getStartAt()),
        match -> new MatchItem(MatchViewModel.create(match, rxBus)));
  }

  public Observable<Observable<Item>> insertDayHeader(
      Observable<GroupedObservable<String, Item>> groupedMatches) {
    return groupedMatches.map(stringItemGroupedObservable -> stringItemGroupedObservable.startWith(
        Observable.just(
            new DayHeaderItem(DayHeaderViewModel.create(stringItemGroupedObservable.getKey())))));
  }

  public Filter getFilter(int pageIndex) {
    int offset = pageIndex * MATCH_PER_PAGE;
    return Filter.create(MATCH_PER_PAGE, offset);
  }
}
