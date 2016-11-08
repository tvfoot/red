package io.oldering.tvfoot.red.viewmodel;


import com.genius.groupie.Item;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import io.oldering.tvfoot.red.api.MatchService;
import io.oldering.tvfoot.red.model.Match;
import io.oldering.tvfoot.red.util.rxbus.RxBus;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.oldering.tvfoot.red.view.item.DayHeaderItem;
import io.oldering.tvfoot.red.view.item.MatchItem;
import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;

// TODO(benoit) delete and only use MatchViewModel / WHY ?
public class MatchListViewModel {
    private final MatchService matchService;
    private final BaseSchedulerProvider schedulerProvider;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
    private final RxBus rxBus;

    @Inject
    public MatchListViewModel(MatchService matchService, BaseSchedulerProvider schedulerProvider, RxBus rxBus) {
        this.matchService = matchService;
        this.schedulerProvider = schedulerProvider;
        this.rxBus = rxBus;

        simpleDateFormat.setTimeZone(TimeZone.getDefault());
    }

    public Observable<Item> getMatches(int pageIndex) {
        // TODO(benoit) should pass the filter as a param?
        Observable<Match> matches = findFuture(getFilter(pageIndex * 30));

        Observable<GroupedObservable<String, Item>> groupedMatches = groupByDate(matches);

        Observable<Observable<Item>> groupedMatchesWithHeader = insertDayHeader(groupedMatches);

        return groupedMatchesWithHeader
                .flatMap(itemObservable -> itemObservable)
                .subscribeOn(schedulerProvider.io());
    }

    public Observable<Match> findFuture(String filter) {
        return matchService
                .findFuture(filter)
                .toObservable()
                .flatMap(Observable::fromIterable);
    }

    public Observable<GroupedObservable<String, Item>> groupByDate(Observable<Match> matches) {
        return matches.groupBy(
                match -> simpleDateFormat.format(match.getStartAt()),
                match -> new MatchItem(MatchViewModel.create(match, rxBus))
        );
    }

    public Observable<Observable<Item>> insertDayHeader(Observable<GroupedObservable<String, Item>> groupedMatches) {
        return groupedMatches.map(stringItemGroupedObservable -> stringItemGroupedObservable
                .startWith(
                        Observable.just(
                                new DayHeaderItem(
                                        DayHeaderViewModel.create(
                                                stringItemGroupedObservable.getKey()
                                        )
                                )
                        )
                ));
    }

    public String getFilter(int offset) {
        return "{\"where\":{\"deleted\":{\"neq\":1}},\"order\":\"start-at ASC, weight ASC\",\"limit\":30,\"offset\":" + offset + "}";
    }
}
