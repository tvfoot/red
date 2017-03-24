package io.oldering.tvfoot.red.match;

import io.oldering.tvfoot.red.di.component.TestComponent;
import io.oldering.tvfoot.red.util.Fixture;
import io.oldering.tvfoot.red.util.InjectionContainer;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;

import static io.oldering.tvfoot.red.match.MatchViewState.Status.MATCH_LOADED;
import static io.oldering.tvfoot.red.match.MatchViewState.Status.MATCH_LOADING;

public class MatchInteractorTest {
  private Fixture fixture;
  private MatchInteractor interactor;

  @Before public void setup() {
    TestComponent testComponent = new InjectionContainer().testComponent();
    fixture = testComponent.fixture();
    interactor = testComponent.matchInteractor();
  }

  @Test public void loadMatch() {
    TestObserver<MatchViewState> testObserver = interactor.loadMatch("1").test();

    testObserver.assertComplete();
    testObserver.assertValueCount(2);

    testObserver.assertValueAt(0,
        viewState -> viewState.equals(MatchViewState.builder().status(MATCH_LOADING).build()));

    testObserver.assertValueAt(1, viewState -> viewState.equals(MatchViewState.builder()
        .status(MATCH_LOADED)
        .match(Observable.just(fixture.anyMatch()).map(MatchDisplayable::fromMatch).blockingFirst())
        .build()));
  }
}
