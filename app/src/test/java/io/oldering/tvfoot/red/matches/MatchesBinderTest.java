package io.oldering.tvfoot.red.matches;

import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import io.oldering.tvfoot.red.util.schedulers.ImmediateSchedulerProvider;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MatchesBinderTest {
  @Mock Observable<MatchesIntent> intentObservable;
  @Mock Observable<MatchesViewState> modelObservable;
  @Mock Observable<MatchesViewState> modelObservableOnMainThread;
  @Mock MatchesActivity activity;
  @Mock MatchesInteractor repository;
  private MatchesBinder binder;

  @Before public void setup() {
    MockitoAnnotations.initMocks(this);
    binder = spy(new MatchesBinder(activity, repository, new ImmediateSchedulerProvider()));
  }

  // TODO(benoit) am I testing the right things ?
  @Test public void intent() {
    PublishSubject<MatchesIntent> loadFirstPageSubject = PublishSubject.create();
    PublishSubject<MatchesIntent> loadNextPageSubject = PublishSubject.create();
    PublishSubject<MatchesIntent> matchRowClickSubject = PublishSubject.create();
    when(activity.loadFirstPageIntent()).thenReturn(loadFirstPageSubject);
    when(activity.loadNextPageIntent()).thenReturn(loadNextPageSubject);
    when(activity.matchRowClickIntent()).thenReturn(matchRowClickSubject);

    TestObserver<MatchesIntent> testObserver = binder.intent().test();

    assertEquals(0, testObserver.valueCount());

    MatchesIntent.LoadFirstPage loadFirstPageIntent = MatchesIntent.LoadFirstPage.create();
    MatchesIntent.LoadNextPage loadNextPageIntentA = MatchesIntent.LoadNextPage.create(1);
    MatchesIntent.LoadNextPage loadNextPageIntentB = MatchesIntent.LoadNextPage.create(2);
    MatchesIntent.MatchRowClick matchRowClickIntent =
        MatchesIntent.MatchRowClick.create(mock(MatchRowDisplayable.class));

    loadFirstPageSubject.onNext(loadFirstPageIntent);
    loadFirstPageSubject.onNext(loadNextPageIntentA);
    loadFirstPageSubject.onNext(loadNextPageIntentB);
    loadFirstPageSubject.onNext(matchRowClickIntent);

    testObserver.assertValueAt(0, v -> v.equals(loadFirstPageIntent));
    testObserver.assertValueAt(1, v -> v.equals(loadNextPageIntentA));
    testObserver.assertValueAt(2, v -> v.equals(loadNextPageIntentB));
    testObserver.assertValueAt(3, v -> v.equals(matchRowClickIntent));
  }

  @Test public void model() {
    // don't know yet how to test this
  }

  @Test public void view() {
    MatchesViewState state = mock(MatchesViewState.class);
    PublishSubject<MatchesViewState> stateSubject = PublishSubject.create();

    binder.view(stateSubject);
    verify(activity, never()).render(any(MatchesViewState.class));

    stateSubject.onNext(state);
    verify(activity).render(state);
  }

  @Test public void bind() {
    doReturn(intentObservable).when(binder).intent();
    doReturn(modelObservable).when(binder).model(intentObservable);

    binder.bind();

    verify(binder).intent();
    verify(binder).model(intentObservable);
    verify(binder).view(modelObservable);
  }
}
