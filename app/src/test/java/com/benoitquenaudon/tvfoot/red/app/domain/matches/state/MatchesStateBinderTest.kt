package com.benoitquenaudon.tvfoot.red.app.domain.matches.state

import com.benoitquenaudon.tvfoot.red.app.common.firebase.NoopRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.ImmediateSchedulerProvider
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates

class MatchesStateBinderTest {
  var stateBinder: MatchesStateBinder by Delegates.notNull<MatchesStateBinder>()
  val testObserver: TestObserver<MatchesViewState> by lazy {
    stateBinder.states().test()
  }

  @Before fun setup() {
    stateBinder = MatchesStateBinder(
        PublishSubject.create<MatchesIntent>(),
        PublishSubject.create<MatchesViewState>(),
        FakeMatchesRepository(),
        ImmediateSchedulerProvider(),
        NoopRedFirebaseAnalytics
    )
  }

  @Test fun testA() {
    stateBinder.processIntents(Observable.just(MatchesIntent.InitialIntent))

    testObserver.assertNotComplete()
    testObserver.assertValueAt(0, { state -> state.refreshLoading })
    testObserver.assertValueAt(1, { state -> !state.refreshLoading && state.matches.isNotEmpty() })
  }
}