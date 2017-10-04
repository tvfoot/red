package com.benoitquenaudon.tvfoot.red.app.domain.match.state

import com.benoitquenaudon.tvfoot.red.app.common.PreferenceRepository
import com.benoitquenaudon.tvfoot.red.app.common.firebase.BaseRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.firebase.NoopRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.notification.NotificationRepository
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.ImmediateSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.data.source.FakeMatchRepository
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchIntent
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchIntent.InitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchViewModel
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchViewState
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
import javax.inject.Inject

class MatchStateBinderTest {
  lateinit var matchStateBinder: MatchViewModel
  lateinit var testObserver: TestObserver<MatchViewState>
  val preferenceRepository: PreferenceRepository = mock(PreferenceRepository::class.java)
  val notificationRepository: NotificationRepository = mock(NotificationRepository::class.java)
  @Inject lateinit var fixture: Fixture

  @Before
  fun setup() {
    InjectionContainer.testComponentInstance.inject(this)

    val intents = PublishSubject.create<MatchIntent>()
    val states = PublishSubject.create<MatchViewState>()
    val schedulerProvider: BaseSchedulerProvider = ImmediateSchedulerProvider()
    val redFirebaseAnalytics: BaseRedFirebaseAnalytics = NoopRedFirebaseAnalytics

    matchStateBinder = MatchViewModel(
        intents,
        states,
        FakeMatchRepository(),
        preferenceRepository,
        notificationRepository,
        schedulerProvider,
        redFirebaseAnalytics
    )

    matchStateBinder.states().test()
  }

  @Ignore @Test fun initialIntentLoadMatch() {
    val matchId = "matchId"

    `when`(preferenceRepository.loadNotifyMatchStart(matchId)).thenReturn(Single.just(false))

    matchStateBinder.processIntents(Observable.just(InitialIntent(matchId)))

    testObserver.assertValueAt(0, { it.loading })
    testObserver.assertValueAt(1) { (match, _, loading, shouldNotifyMatchStart) ->
      !loading &&
          match != null &&
          match.matchId == matchId &&
          !shouldNotifyMatchStart
    }
  }
}