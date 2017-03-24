package io.oldering.tvfoot.red.match;

import io.oldering.tvfoot.red.util.schedulers.ImmediateSchedulerProvider;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.oldering.tvfoot.red.match.MatchViewState.Status.MATCH_LOADING;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MatchBinderTest {
  @Mock Observable<MatchIntent> intentObservable;
  @Mock Observable<MatchViewState> modelObservable;
  @Mock Observable<MatchViewState> modelObservableOnMainThread;
  @Mock MatchActivity activity;
  @Mock MatchInteractor interactor;
  private MatchBinder binder;

  @Before public void setup() {
    MockitoAnnotations.initMocks(this);
    binder = spy(new MatchBinder(activity, interactor, new ImmediateSchedulerProvider()));
  }

  // TODO(benoit) am I testing the right things ?
  @Test public void intent() {
    PublishSubject<MatchIntent> loadMatchSubject = PublishSubject.create();
    when(activity.loadMatchIntent()).thenReturn(loadMatchSubject);

    TestObserver<MatchIntent> testObserver = binder.intent().test();

    assertEquals(0, testObserver.valueCount());

    MatchIntent.LoadMatch loadMatchIntent = MatchIntent.LoadMatch.create("1");
    loadMatchSubject.onNext(loadMatchIntent);

    testObserver.assertValueAt(0, v -> v.equals(loadMatchIntent));
  }

  @Ignore("don't know why the testObserver is waiting on something") @Test public void model()
      throws InterruptedException {
    when(interactor.loadMatch("1")).thenReturn(modelObservable);

    PublishSubject<MatchIntent> loadMatchSubject = PublishSubject.create();
    when(activity.loadMatchIntent()).thenReturn(loadMatchSubject);
    TestObserver<MatchViewState> testObserver = binder.model(loadMatchSubject).test();

    verify(interactor, never()).loadMatch(anyString());

    loadMatchSubject.onNext(MatchIntent.LoadMatch.create("1"));
    verify(interactor).loadMatch("1");

    // What is is waiting for ?
    testObserver.assertValueCount(2);
    testObserver.assertValueAt(0,
        state -> state.equals(MatchViewState.builder().status(MATCH_LOADING).build()));
  }

  @Test public void view() {
    MatchViewState state = mock(MatchViewState.class);
    PublishSubject<MatchViewState> stateSubject = PublishSubject.create();

    binder.view(stateSubject);
    verify(activity, never()).render(any(MatchViewState.class));

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
