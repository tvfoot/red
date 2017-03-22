package io.oldering.tvfoot.red.matches;

import io.oldering.tvfoot.red.di.component.TestComponent;
import io.oldering.tvfoot.red.matches.displayable.HeaderRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.LoadingRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import io.oldering.tvfoot.red.matches.displayable.MatchesItemDisplayable;
import io.oldering.tvfoot.red.util.Fixture;
import io.oldering.tvfoot.red.util.InjectionContainer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_ERROR;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADED;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADING;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.MATCH_ROW_CLICK;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_ERROR;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_LOADED;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_LOADING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MatchesViewStateTest {
  private Fixture fixture;

  @Before public void setup() {
    TestComponent testComponent = new InjectionContainer().testComponent();
    fixture = testComponent.fixture();
  }

  @Test public void matchesItemDisplayables_noLoadingIfEmpty() {
    MatchesViewState state = MatchesViewState.builder().status(FIRST_PAGE_LOADED).build();

    List<MatchesItemDisplayable> matchesItemDisplayables = state.matchesItemDisplayables();

    assertTrue(matchesItemDisplayables.isEmpty());
  }

  @Test public void matchesItemDisplayables_hasLoadingIfNotEmpty() {
    MatchesViewState state = MatchesViewState.builder()
        .status(FIRST_PAGE_LOADED)
        .matches(Collections.singletonList(MatchRowDisplayable.fromMatch(fixture.anyMatch())))
        .build();

    List<MatchesItemDisplayable> matchesItemDisplayables = state.matchesItemDisplayables();

    assertEquals(3, matchesItemDisplayables.size());
    assertEquals(matchesItemDisplayables.get(2), LoadingRowDisplayable.create());
  }

  @Test public void matchesItemDisplayables_includeHeadersButNoDuplicate() {
    List<MatchRowDisplayable> matches = MatchRowDisplayable.fromMatches(fixture.anyMatchesShort());
    //13, 13, 14, 16, 16
    assertEquals(5, matches.size());

    MatchesViewState state =
        MatchesViewState.builder().status(FIRST_PAGE_LOADED).matches(matches).build();
    List<MatchesItemDisplayable> matchesItemDisplayables = state.matchesItemDisplayables();

    // 5 matches + 3 headers + loading
    assertEquals(9, matchesItemDisplayables.size());
    assertEquals(matchesItemDisplayables.get(0), HeaderRowDisplayable.create("2016-11-13"));
    assertTrue(matchesItemDisplayables.get(1) instanceof MatchRowDisplayable);
    assertTrue(matchesItemDisplayables.get(2) instanceof MatchRowDisplayable);
    assertEquals(matchesItemDisplayables.get(3), HeaderRowDisplayable.create("2016-11-14"));
    assertTrue(matchesItemDisplayables.get(4) instanceof MatchRowDisplayable);
    assertEquals(matchesItemDisplayables.get(5), HeaderRowDisplayable.create("2016-11-16"));
    assertTrue(matchesItemDisplayables.get(6) instanceof MatchRowDisplayable);
    assertTrue(matchesItemDisplayables.get(7) instanceof MatchRowDisplayable);
    assertEquals(matchesItemDisplayables.get(8), LoadingRowDisplayable.create());
  }

  @Test public void matchRowClick() {
    MatchRowDisplayable match = mock(MatchRowDisplayable.class);
    MatchesViewState state = MatchesViewState.matchRowClick(match);

    assertEquals(match, state.match());
    assertEquals(MATCH_ROW_CLICK, state.status());
  }

  @Test public void reduce_firstPageLoading() {
    MatchesViewState previousState = MatchesViewState.builder().status(FIRST_PAGE_LOADING).build();
    MatchesViewState partialState = MatchesViewState.builder().status(FIRST_PAGE_LOADING).build();

    MatchesViewState newState = MatchesViewState.reduce(previousState, partialState);
    assertTrue(newState.firstPageLoading());
    assertNull(newState.firstPageError());
    assertEquals(FIRST_PAGE_LOADING, newState.status());
  }

  @Test public void reduce_firstPageLoaded() {
    List<MatchRowDisplayable> matches = MatchRowDisplayable.fromMatches(fixture.anyMatches());

    MatchesViewState previousState = MatchesViewState.builder().status(FIRST_PAGE_LOADING).build();
    MatchesViewState partialState =
        MatchesViewState.builder().status(FIRST_PAGE_LOADED).matches(matches).build();

    MatchesViewState newState = MatchesViewState.reduce(previousState, partialState);
    assertFalse(newState.firstPageLoading());
    assertNull(newState.firstPageError());
    assertEquals(FIRST_PAGE_LOADED, newState.status());
    assertEquals(matches, newState.matches());
  }

  @Test public void reduce_firstPageError() {
    Throwable throwable = mock(Throwable.class);

    MatchesViewState previousState = MatchesViewState.builder().status(FIRST_PAGE_LOADING).build();
    MatchesViewState partialState =
        MatchesViewState.builder().status(FIRST_PAGE_ERROR).firstPageError(throwable).build();

    MatchesViewState newState = MatchesViewState.reduce(previousState, partialState);
    assertFalse(newState.firstPageLoading());
    assertEquals(throwable, newState.firstPageError());
    assertEquals(FIRST_PAGE_ERROR, newState.status());
  }

  @Test public void reduce_nextPageLoading() {
    List<MatchRowDisplayable> previousMatches =
        MatchRowDisplayable.fromMatches(fixture.anyMatches());

    MatchesViewState previousState =
        MatchesViewState.builder().status(FIRST_PAGE_LOADED).matches(previousMatches).build();
    MatchesViewState partialState = MatchesViewState.builder().status(NEXT_PAGE_LOADING).build();

    MatchesViewState newState = MatchesViewState.reduce(previousState, partialState);
    assertTrue(newState.nextPageLoading());
    assertNull(newState.nextPageError());
    assertEquals(previousMatches, newState.matches());
    assertEquals(NEXT_PAGE_LOADING, newState.status());
  }

  @Test public void reduce_nextPageLoaded() {
    List<MatchRowDisplayable> previousMatches =
        MatchRowDisplayable.fromMatches(fixture.anyMatches());
    List<MatchRowDisplayable> nextMatches =
        MatchRowDisplayable.fromMatches(fixture.anyNextMatches());

    MatchesViewState previousState =
        MatchesViewState.builder().status(NEXT_PAGE_LOADING).matches(previousMatches).build();
    MatchesViewState partialState =
        MatchesViewState.builder().status(NEXT_PAGE_LOADED).matches(nextMatches).build();

    MatchesViewState newState = MatchesViewState.reduce(previousState, partialState);
    assertFalse(newState.nextPageLoading());
    assertNull(newState.nextPageError());
    assertEquals(NEXT_PAGE_LOADED, newState.status());

    List<MatchRowDisplayable> newMatches = new ArrayList<>();
    newMatches.addAll(previousMatches);
    newMatches.addAll(nextMatches);
    assertEquals(newMatches, newState.matches());
  }

  @Test public void reduce_nextPageError() {
    List<MatchRowDisplayable> previousMatches =
        MatchRowDisplayable.fromMatches(fixture.anyMatches());
    Throwable throwable = mock(Throwable.class);

    MatchesViewState previousState =
        MatchesViewState.builder().status(NEXT_PAGE_LOADING).matches(previousMatches).build();
    MatchesViewState partialState =
        MatchesViewState.builder().status(NEXT_PAGE_ERROR).nextPageError(throwable).build();

    MatchesViewState newState = MatchesViewState.reduce(previousState, partialState);
    assertFalse(newState.nextPageLoading());
    assertEquals(throwable, newState.nextPageError());
    assertEquals(NEXT_PAGE_ERROR, newState.status());
  }

  @Ignore("not implemented yet") @Test public void reduce_pullToRefreshLoading() {
  }

  @Ignore("not implemented yet") @Test public void reduce_pullToRefreshLoaded() {
  }

  @Ignore("not implemented yet") @Test public void reduce_pullToRefreshError() {
  }

  @Test public void reduce_matchRowClick() {
    MatchesViewState previousState = mock(MatchesViewState.class);
    MatchesViewState partialState = mock(MatchesViewState.class);
    when(partialState.status()).thenReturn(MATCH_ROW_CLICK);
    MatchesViewState newState = MatchesViewState.reduce(previousState, partialState);

    assertEquals(partialState, newState);
  }
}
