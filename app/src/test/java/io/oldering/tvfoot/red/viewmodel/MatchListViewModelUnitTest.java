package io.oldering.tvfoot.red.viewmodel;

import com.genius.groupie.Item;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.oldering.tvfoot.red.api.MatchService;
import io.oldering.tvfoot.red.di.component.DaggerTestComponent;
import io.oldering.tvfoot.red.di.component.TestComponent;
import io.oldering.tvfoot.red.di.module.NetworkModule;
import io.oldering.tvfoot.red.model.Match;
import io.oldering.tvfoot.red.model.search.Filter;
import io.oldering.tvfoot.red.util.Fixture;
import io.oldering.tvfoot.red.util.rxbus.RxBus;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.oldering.tvfoot.red.view.item.DayHeaderItem;
import io.oldering.tvfoot.red.view.item.MatchItem;
import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class MatchListViewModelUnitTest {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
    BaseSchedulerProvider schedulerProvider;

    MockWebServer server;
    TestComponent component;

    @Before
    public void before() throws IOException {
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

        server = new MockWebServer();
        server.start();
        HttpUrl baseUrl = server.url("/");
        component = DaggerTestComponent.builder().networkModule(new NetworkModule(baseUrl.toString())).build();
        schedulerProvider = component.schedulerProvider();
    }

    @Test
    public void findFuture() throws IOException {

        // setup
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("sample_data2.json");
        Fixture fixture = component.fixture();
        String matchesResponse = fixture.readInputStream(is);
        server.enqueue(new MockResponse().setBody(matchesResponse));
        MatchListViewModel matchListViewModel = component.matchListViewModel();

        // execute
        Observable<Match> matches = matchListViewModel.findFuture(matchListViewModel.getFilter(0));

        // verifity
        is = this.getClass().getClassLoader().getResourceAsStream("sample_data2.json");
        matches.test()
                .assertResult(fixture.readJsonStream(is).toArray(new Match[30]))
                .assertValueCount(30)
                .assertComplete();
    }

    @Test
    public void groupByDate() throws IOException {
        // setup
        Fixture fixture = component.fixture();
        MatchListViewModel matchListViewModel = component.matchListViewModel();

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("sample_data2.json");
        List<Match> matchesAsList = fixture.readJsonStream(is);
        Observable<Match> matches = Observable.fromIterable(matchesAsList);

        // execute
        Observable<GroupedObservable<String, Item>> groupedMatchesObservable = matchListViewModel.groupByDate(matches);

        // verifty
        List<List<Item>> expectedGroupedObservables = createGroupedObservableItems(component, matchesAsList);

        // container
        groupedMatchesObservable.test()
                .assertComplete()
                .assertValueCount(expectedGroupedObservables.size());

        // each groupedObservable
        List<GroupedObservable<String, Item>> groupedObservables = groupedMatchesObservable.toList().blockingGet();
        for (int i = 0; i < groupedObservables.size(); i++) {
            List<Item> expectedItems = expectedGroupedObservables.get(i);
            GroupedObservable<String, Item> groupedObservable = groupedObservables.get(i);

            groupedObservable.test()
                    .assertComplete()
                    .assertResult(expectedItems.toArray(new Item[expectedItems.size()]));
        }
    }

    @Test
    public void insertDayHeader() throws IOException {
        // setup
        Fixture fixture = component.fixture();
        MatchListViewModel matchListViewModel = component.matchListViewModel();

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("sample_data2.json");
        List<Match> matchesAsList = fixture.readJsonStream(is);
        Observable<Match> matches = Observable.fromIterable(matchesAsList);

        Observable<GroupedObservable<String, Item>> groupedMatchesObservable = matchListViewModel.groupByDate(matches);

        // execute
        Observable<Observable<Item>> withDayHeadersGroupedMatches = matchListViewModel.insertDayHeader(groupedMatchesObservable);

        // verify
        List<List<Item>> expectedItemsObservables = createItemObservablesWithHeader(component, matchesAsList);

        // container
        withDayHeadersGroupedMatches.test()
                .assertComplete()
                .assertValueCount(expectedItemsObservables.size());

        // each groupedObservable
        List<Observable<Item>> itemObservables = withDayHeadersGroupedMatches.toList().blockingGet();
        for (int i = 0; i < itemObservables.size(); i++) {
            List<Item> expectedItems = expectedItemsObservables.get(i);
            Observable<Item> itemObservable = itemObservables.get(i);

            itemObservable.test()
                    .assertComplete()
                    .assertResult(expectedItems.toArray(new Item[expectedItems.size()]));
        }
    }

    @Test
    public void getMatches() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("sample_data2.json");
        Fixture fixture = component.fixture();
        String matchesResponse = fixture.readInputStream(is);
        server.enqueue(new MockResponse().setBody(matchesResponse));

        MatchListViewModel matchListViewModel = component.matchListViewModel();

        is = this.getClass().getClassLoader().getResourceAsStream("sample_data2.json");
        List<Match> matchesAsList = fixture.readJsonStream(is);

        Filter filter = matchListViewModel.getFilter(1);

        // execute
        Observable<Item> items = matchListViewModel.getMatches(filter);

        // verify
        List<Item> expectedItems = createFlattenedItemWithHeader(component, matchesAsList);

        items
                .observeOn(schedulerProvider.ui())
                .test()
                .assertComplete()
                .assertValues(expectedItems.toArray(new Item[expectedItems.size()]));
    }

    @SuppressWarnings("Convert2MethodRef")
    private List<Item> createFlattenedItemWithHeader(TestComponent component, List<Match> matchesAsList) {
        List<List<Item>> itemLists = createItemObservablesWithHeader(component, matchesAsList);

        List<Item> items = new ArrayList<>(50);
        for (int i = 0; i < itemLists.size(); i++) {
            items.addAll(itemLists.get(i));
        }
        return items;
    }

    private List<List<Item>> createItemObservablesWithHeader(TestComponent component, List<Match> matchesAsList) {
        List<List<Item>> matchItemsAsList = new ArrayList<>();

        HashMap<String, List<Item>> matchItemsHashMap = new LinkedHashMap<>();
        for (Match match : matchesAsList) {
            String key = simpleDateFormat.format(match.getStartAt());
            List<Item> items = matchItemsHashMap.get(key);
            if (items == null) {
                items = new ArrayList<>();
                items.add(new DayHeaderItem(DayHeaderViewModel.create(key)));
                matchItemsHashMap.put(key, items);
            }
            items.add(new MatchItem(MatchViewModel.create(match, component.rxBus())));
        }
        for (Map.Entry<String, List<Item>> entry : matchItemsHashMap.entrySet()) {
            matchItemsAsList.add(entry.getValue());
        }

        return matchItemsAsList;
    }

    private List<List<Item>> createGroupedObservableItems(TestComponent component, List<Match> matchesAsList) {
        List<List<Item>> matchItemsAsList = new ArrayList<>();

        HashMap<String, List<Item>> matchItemsHashMap = new LinkedHashMap<>();
        for (Match match : matchesAsList) {
            String key = simpleDateFormat.format(match.getStartAt());
            List<Item> items = matchItemsHashMap.get(key);
            if (items == null) {
                items = new ArrayList<>();
                matchItemsHashMap.put(key, items);
            }
            items.add(new MatchItem(MatchViewModel.create(match, component.rxBus())));
        }
        for (Map.Entry<String, List<Item>> entry : matchItemsHashMap.entrySet()) {
            matchItemsAsList.add(entry.getValue());
        }

        return matchItemsAsList;
    }

    @Test
    public void getFilter() {
        MatchService matchService = mock(MatchService.class);

        RxBus rxBus = mock(RxBus.class);
        MatchListViewModel matchListVM = new MatchListViewModel(matchService, schedulerProvider, rxBus);

        String filter = "{\"where\":{\"deleted\":{\"neq\":1}},\"order\":\"start-at ASC, weight ASC\",\"limit\":30,\"offset\":30}";
        assertEquals(matchListVM.getFilter(1).toString(), filter);
    }
}
