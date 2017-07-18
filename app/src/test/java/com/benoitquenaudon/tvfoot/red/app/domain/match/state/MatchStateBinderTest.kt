package com.benoitquenaudon.tvfoot.red.app.domain.match.state

import com.benoitquenaudon.tvfoot.red.app.common.PreferenceService
import com.benoitquenaudon.tvfoot.red.app.common.firebase.BaseRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.firebase.NoopRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.notification.NotificationService
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.ImmediateSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchIntent.InitialIntent
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import kotlin.properties.Delegates

class MatchStateBinderTest {
  var matchStateBinder: MatchStateBinder by Delegates.notNull<MatchStateBinder>()
  val testObserver: TestObserver<MatchViewState> by lazy {
    matchStateBinder.statesAsObservable.test()
  }

  @Before
  fun setup() {
    val intents = PublishSubject.create<MatchIntent>()
    val states = PublishSubject.create<MatchViewState>()
    val matchService = mock(MatchService::class.java)
    val preferenceService = mock(PreferenceService::class.java)
    val notificationService = mock(NotificationService::class.java)
    val schedulerProvider: BaseSchedulerProvider = ImmediateSchedulerProvider()
    val redFirebaseAnalytics: BaseRedFirebaseAnalytics = NoopRedFirebaseAnalytics

    matchStateBinder = MatchStateBinder(
        intents,
        states,
        matchService,
        preferenceService,
        notificationService,
        schedulerProvider,
        redFirebaseAnalytics
    )
  }

  @Test
  fun initialIntentLoadMatch() {
    matchStateBinder.forwardIntents(Observable.just(InitialIntent.create("matchid")))
    // TODO(benoit)
//    testObserver.asse
  }
}