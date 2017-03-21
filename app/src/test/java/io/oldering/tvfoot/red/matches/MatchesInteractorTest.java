package io.oldering.tvfoot.red.matches;

import io.oldering.tvfoot.red.di.component.TestComponent;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import io.oldering.tvfoot.red.util.Fixture;
import io.oldering.tvfoot.red.util.InjectionContainer;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;

import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADED;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADING;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_LOADED;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_LOADING;

public class MatchesInteractorTest {
  private Fixture fixture;
  private MatchesInteractor interactor;

  @Before public void setup() {
    TestComponent testComponent = new InjectionContainer().testComponent();
    fixture = testComponent.fixture();
    interactor = testComponent.matchesInteractor();
  }

  @Test public void loadFirstPage() {
    TestObserver<MatchesViewState> testObserver = interactor.loadFirstPage().test();

    testObserver.assertComplete();

    testObserver.assertValueAt(0, viewState -> viewState.equals(
        MatchesViewState.builder().status(FIRST_PAGE_LOADING).build()));

    testObserver.assertValueAt(1, viewState -> viewState.equals(MatchesViewState.builder()
        .status(FIRST_PAGE_LOADED)
        .matches(Observable.just(fixture.anyMatches())
            .map(MatchRowDisplayable::fromMatches)
            .blockingFirst())
        .build()));
  }

  @Test public void loadNextPage() {
    TestObserver<MatchesViewState> testObserver = interactor.loadNextPage(1).test();

    testObserver.assertComplete();

    testObserver.assertValueAt(0, viewState -> viewState.equals(
        MatchesViewState.builder().status(NEXT_PAGE_LOADING).build()));

    testObserver.assertValueAt(1, viewState -> viewState.equals(MatchesViewState.builder()
        .status(NEXT_PAGE_LOADED)
        .matches(Observable.just(fixture.anyNextMatches())
            .map(MatchRowDisplayable::fromMatches)
            .blockingFirst())
        .build()));
  }
}
