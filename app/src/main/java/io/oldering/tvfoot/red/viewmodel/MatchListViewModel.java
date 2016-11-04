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
import io.reactivex.functions.Function;

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
        // TODO(benoit) split into functions so gets easy to test
        return matchService
                .findFuture(getFilter(pageIndex * 30))
                .toObservable()
                .flatMap(Observable::fromIterable)
                .groupBy(
                        match -> simpleDateFormat.format(match.getStartAt()),
                        (Function<Match, Item>) match -> new MatchItem(MatchViewModel.create(match, rxBus))
                )
                .map(stringItemGroupedObservable -> stringItemGroupedObservable
                        .startWith(
                                Observable.just(
                                        new DayHeaderItem(
                                                DayHeaderViewModel.create(
                                                        stringItemGroupedObservable.getKey()
                                                )
                                        )
                                )
                        ))
                .flatMap(itemObservable -> itemObservable)
                .subscribeOn(schedulerProvider.io());
    }

    public String getFilter(int offset) {
        return "{\"where\":{\"deleted\":{\"neq\":1}},\"order\":\"start-at ASC, weight ASC\",\"limit\":30,\"offset\":" + offset + "}";
    }
}
