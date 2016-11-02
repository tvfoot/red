package io.oldering.tvfoot.red;

import com.genius.groupie.Item;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.oldering.tvfoot.red.api.MatchService;
import io.oldering.tvfoot.red.model.Competition;
import io.oldering.tvfoot.red.model.Match;
import io.oldering.tvfoot.red.model.Team;
import io.oldering.tvfoot.red.util.rxbus.RxBus;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.oldering.tvfoot.red.util.schedulers.ImmediateSchedulerProvider;
import io.oldering.tvfoot.red.viewmodel.MatchListViewModel;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static io.oldering.tvfoot.red.viewmodel.DayHeaderViewModel.ONE_DAY_IN_MILLIS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MatchListViewModelUnitTest {
    private MatchListViewModel matchListVM;
    MatchService matchService;
    private BaseSchedulerProvider schedulerProvider;

    @Before
    public void before() {
        matchService = mock(MatchService.class);
        schedulerProvider = new ImmediateSchedulerProvider();

        RxBus rxBus = mock(RxBus.class);
        matchListVM = new MatchListViewModel(matchService, schedulerProvider, rxBus);
    }

    @Test
    @Ignore
    public void getMatches() {
        // TODO(benoit) fails because DayHeaderItem constructor depends on Android APIs

        // TODO(benoit) need some cleaning up
        // am I on the right track ?

        String filter = matchListVM.getFilter(0);

        when(matchService.findFuture(filter)).thenReturn(tasksSingle());

        Observable<Item> tasks$ = matchListVM.getMatches(0);

        TestObserver<Item> testObserver = new TestObserver<>();
        tasks$
                .observeOn(schedulerProvider.ui())
                .subscribe(testObserver);

        verify(matchService).findFuture(filter);
        testObserver.assertComplete();
        testObserver.assertValueCount(8);
//        testObserver.assertValue()
    }

    @Test
    public void getFilter() {
        int offset = 1;
        String filter = "{\"where\":{\"deleted\":{\"neq\":1}},\"order\":\"start-at ASC, weight ASC\",\"limit\":30,\"offset\":" + offset + "}";
        assertEquals(matchListVM.getFilter(offset), filter);
    }

    // TODO(benoit) create like 5 matchs on three different days
    private Single<List<Match>> tasksSingle() {
        List<Match> matches = new ArrayList<>(5);

        matches.add(createMatch("A", 0));
        matches.add(createMatch("B", 0));
        matches.add(createMatch("C", 1));
        matches.add(createMatch("D", 2));
        matches.add(createMatch("E", 2));

        return Single.just(matches);
    }

    private Match createMatch(String code, int delayInDays) {
        return Match.create(
                "id",
                "label" + code,
                new Date(System.currentTimeMillis() + (delayInDays * ONE_DAY_IN_MILLIS)),
                "matchDay" + code,
                Team.create(
                        "homeCode" + code,
                        "home" + code,
                        "homeFull" + code,
                        "homeCity" + code,
                        "homeCountry" + code,
                        "homeUrl" + code,
                        "homeStadium" + code,
                        null,
                        "homeType" + code),
                Team.create(
                        "awayCode" + code,
                        "away" + code,
                        "awayFull" + code,
                        "awayCity" + code,
                        "awayCountry" + code,
                        "awayUrl" + code,
                        "awayStadium" + code,
                        null,
                        "awayType" + code),
                new ArrayList<>(),
                "place" + code,
                Competition.create(
                        "code" + code,
                        "name" + code,
                        null,
                        null,
                        null),
                false
        );
    }
}
