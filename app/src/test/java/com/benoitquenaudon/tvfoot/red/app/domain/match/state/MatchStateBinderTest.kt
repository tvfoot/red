package com.benoitquenaudon.tvfoot.red.app.domain.match.state

import com.benoitquenaudon.tvfoot.red.app.common.PreferenceService
import com.benoitquenaudon.tvfoot.red.app.common.firebase.BaseRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.firebase.NoopRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.notification.NotificationService
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.ImmediateSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchIntent.InitialIntent
import com.benoitquenaudon.tvfoot.red.testutil.Fixture
import com.benoitquenaudon.tvfoot.red.testutil.InjectionContainer
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import kotlin.properties.Delegates

class MatchStateBinderTest {
  var matchStateBinder: MatchStateBinder by Delegates.notNull<MatchStateBinder>()
  val testObserver: TestObserver<MatchViewState> by lazy {
    matchStateBinder.statesAsObservable().test()
  }
  val matchService: MatchService = mock(MatchService::class.java)
  val preferenceService: PreferenceService = mock(PreferenceService::class.java)
  val notificationService: NotificationService = mock(NotificationService::class.java)
  val fixture: Fixture = InjectionContainer.testComponentInstance.fixture()

  @Before
  fun setup() {
    val intents = PublishSubject.create<MatchIntent>()
    val states = PublishSubject.create<MatchViewState>()
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

  @Test @Ignore("not done yet")
  fun initialIntentLoadMatch() {
    val matchId = "matchId"

    `when`(matchService.loadMatch(matchId)).thenReturn(Single.just(fixture.anyMatch()))
    `when`(preferenceService.loadNotifyMatchStart(matchId)).thenReturn(Single.just(false))

    matchStateBinder.forwardIntents(Observable.just(InitialIntent(matchId)))

    testObserver.assertValueAt(0, { it.loading })
    testObserver.assertValueAt(1) { (match, _, loading, shouldNotifyMatchStart) ->
      !loading &&
          match != null &&
          match.matchId() == matchId &&
          !shouldNotifyMatchStart
    }
  }
}