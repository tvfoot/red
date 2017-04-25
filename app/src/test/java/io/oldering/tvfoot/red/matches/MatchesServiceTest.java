package io.oldering.tvfoot.red.matches;

import io.oldering.tvfoot.red.di.component.TestComponent;
import io.oldering.tvfoot.red.matches.state.MatchesService;
import io.oldering.tvfoot.red.util.Fixture;
import io.oldering.tvfoot.red.util.InjectionContainer;
import org.junit.Before;

public class MatchesServiceTest {
  private Fixture fixture;
  private MatchesService interactor;

  @Before public void setup() {
    TestComponent testComponent = new InjectionContainer().testComponent();
    fixture = testComponent.fixture();
    interactor = testComponent.matchesInteractor();
  }

  //@Test public void loadFirstPage() {
  //  TestObserver<MatchesViewState> testObserver = interactor.loadFirstPage().test();
  //
  //  testObserver.assertComplete();
  //
  //  testObserver.assertValueAt(0, viewState -> viewState.equals(
  //      MatchesViewState.builder().status(FIRST_PAGE_IN_FLIGHT).build()));
  //
  //  testObserver.assertValueAt(1, viewState -> viewState.equals(MatchesViewState.builder()
  //      .status(FIRST_PAGE_SUCCESS)
  //      .matches(Observable.just(fixture.anyMatches())
  //          .map(MatchRowDisplayable::fromMatches)
  //          .blockingFirst())
  //      .build()));
  //}
  //
  //@Test public void loadNextPage() {
  //  TestObserver<MatchesViewState> testObserver = interactor.loadNextPage(1).test();
  //
  //  testObserver.assertComplete();
  //
  //  testObserver.assertValueAt(0, viewState -> viewState.equals(
  //      MatchesViewState.builder().status(NEXT_PAGE_IN_FLIGHT).build()));
  //
  //  testObserver.assertValueAt(1, viewState -> viewState.equals(MatchesViewState.builder()
  //      .status(NEXT_PAGE_SUCCESS)
  //      .matches(Observable.just(fixture.anyNextMatches())
  //          .map(MatchRowDisplayable::fromMatches)
  //          .blockingFirst())
  //      .build()));
  //}
}
