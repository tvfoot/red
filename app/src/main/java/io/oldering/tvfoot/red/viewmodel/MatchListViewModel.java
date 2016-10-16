package io.oldering.tvfoot.red.viewmodel;


import com.genius.groupie.Item;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import io.oldering.tvfoot.red.api.MatchService;
import io.oldering.tvfoot.red.di.DaggerAppComponent;
import io.oldering.tvfoot.red.model.Match;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.oldering.tvfoot.red.view.item.DayHeaderItem;
import io.oldering.tvfoot.red.view.item.MatchItem;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

// TODO(benoit) delete and only use MatchViewModel
public class MatchListViewModel {
    private final MatchService matchService;
    @Inject
    BaseSchedulerProvider schedulerProvider;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
    private PublishSubject<Match> subject = PublishSubject.create();
    private int offset = 0;

    @Inject
    public MatchListViewModel(MatchService matchService) {
        this.matchService = matchService;
        DaggerAppComponent.create().inject(this);

        simpleDateFormat.setTimeZone(TimeZone.getDefault());
    }

    /**
     * Well, this is messy
     */
    public Observable<Item> matchStream() {
        return subject
                .groupBy(
                        match -> simpleDateFormat.format(match.getStartAt()),
                        (Function<Match, Item>) match -> new MatchItem(MatchViewModel.create(match))
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
                .subscribeOn(schedulerProvider.computation());
    }

    public void getMatches() {
        matchService
                .findFuture(getFilter(offset))
                .toObservable()
                .flatMap(Observable::fromIterable)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.computation())
                .subscribe(subject::onNext);
    }

    public void getMore() {
        // TODO(benoit) return match but in the same stream as getMatches...
        offset += 30;
        getMatches();
    }

    private String getFilter(int offset) {
        return "{\"where\":{\"deleted\":{\"neq\":1}},\"order\":\"start-at ASC, weight ASC\",\"limit\":30,\"offset\":" + offset + "}";
    }
}
