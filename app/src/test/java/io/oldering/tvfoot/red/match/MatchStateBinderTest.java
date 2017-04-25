package io.oldering.tvfoot.red.match;

import io.oldering.tvfoot.red.match.state.MatchIntent;
import io.oldering.tvfoot.red.match.state.MatchService;
import io.oldering.tvfoot.red.match.state.MatchStateBinder;
import io.oldering.tvfoot.red.match.state.MatchViewState;
import io.oldering.tvfoot.red.util.schedulers.ImmediateSchedulerProvider;
import io.reactivex.Observable;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.spy;

public class MatchStateBinderTest {
  @Mock Observable<MatchIntent> intentObservable;
  @Mock Observable<MatchViewState> modelObservable;
  @Mock Observable<MatchViewState> modelObservableOnMainThread;
  @Mock MatchActivity activity;
  @Mock MatchService service;
  private MatchStateBinder binder;

  @Before public void setup() {
    MockitoAnnotations.initMocks(this);
    binder = spy(new MatchStateBinder(service, new ImmediateSchedulerProvider()));
  }
  //
  //// TODO(benoit) am I testing the right things ?
  //@Test public void intent() {
  //  PublishSubject<MatchIntent> loadMatchSubject = PublishSubject.create();
  //  when(activity.initialIntent()).thenReturn(loadMatchSubject);
  //
  //  TestObserver<MatchIntent> testObserver = binder.intent().test();
  //
  //  assertEquals(0, testObserver.valueCount());
  //
  //  MatchIntent.LoadMatch loadMatchIntent = MatchIntent.LoadMatch.create("1");
  //  loadMatchSubject.onNext(loadMatchIntent);
  //
  //  testObserver.assertValueAt(0, v -> v.equals(loadMatchIntent));
  //}
  //
  //@Ignore("don't know why the testObserver is waiting on something") @Test public void model()
  //    throws InterruptedException {
  //  when(service.loadMatch("1")).thenReturn(modelObservable);
  //
  //  PublishSubject<MatchIntent> loadMatchSubject = PublishSubject.create();
  //  when(activity.initialIntent()).thenReturn(loadMatchSubject);
  //  TestObserver<MatchViewState> testObserver = binder.model(loadMatchSubject).test();
  //
  //  verify(service, never()).loadMatch(anyString());
  //
  //  loadMatchSubject.onNext(MatchIntent.LoadMatch.create("1"));
  //  verify(service).loadMatch("1");
  //
  //  // What is is waiting for ?
  //  testObserver.assertValueCount(2);
  //  testObserver.assertValueAt(0,
  //      state -> state.equals(MatchViewState.builder().status(LOAD_MATCH_IN_FLIGHT).build()));
  //}
  //
  //@Test public void view() {
  //  MatchViewState state = mock(MatchViewState.class);
  //  PublishSubject<MatchViewState> stateSubject = PublishSubject.create();
  //
  //  binder.view(stateSubject);
  //  verify(activity, never()).render(any(MatchViewState.class));
  //
  //  stateSubject.onNext(state);
  //  verify(activity).render(state);
  //}
  //
  //@Test public void bind() {
  //  doReturn(intentObservable).when(binder).intent();
  //  doReturn(modelObservable).when(binder).model(intentObservable);
  //
  //  binder.bind();
  //
  //  verify(binder).intent();
  //  verify(binder).model(intentObservable);
  //  verify(binder).view(modelObservable);
  //}
}
