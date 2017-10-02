package com.benoitquenaudon.tvfoot.red.app.domain.matches.state

import com.benoitquenaudon.tvfoot.red.app.common.firebase.NoopRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.ImmediateSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.data.source.FakeMatchesRepository
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesViewModel
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesViewState
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates

class MatchesStateBinderTest {
  var stateBinder by Delegates.notNull<MatchesViewModel>()
  var testObserver by Delegates.notNull<TestObserver<MatchesViewState>>()

  @Before fun setup() {
    stateBinder = MatchesViewModel(
        PublishSubject.create<MatchesIntent>(),
        FakeMatchesRepository(),
        ImmediateSchedulerProvider(),
        NoopRedFirebaseAnalytics
    )

    testObserver = stateBinder.states().test()
  }

  @Test fun firstInitialIntent_shouldLoadMatches() {
    stateBinder.processIntents(Observable.just(
        MatchesIntent.InitialIntent))

    testObserver.assertValueAt(0, MatchesViewState::refreshLoading)
    testObserver.assertValueAt(1, { state -> !state.refreshLoading && state.matches.isNotEmpty() })
  }

  @Test fun secondInitialIntent_shouldGetLastState() {
    // load matches
    stateBinder.processIntents(Observable.just(
        MatchesIntent.InitialIntent))
    // get last state
    stateBinder.processIntents(Observable.just(
        MatchesIntent.InitialIntent))

    testObserver.assertValueCount(3)
    testObserver.values().let { viewStates ->
      Assert.assertEquals(viewStates[1], viewStates[2])
    }
  }

  @Test fun refreshIntent_shouldLoadNewMatches() {
    // load 2 pages of matches
    stateBinder.processIntents(Observable.just(
        MatchesIntent.LoadNextPageIntent(0)))
    stateBinder.processIntents(Observable.just(
        MatchesIntent.LoadNextPageIntent(1)))
    // refresh
    stateBinder.processIntents(Observable.just(
        MatchesIntent.RefreshIntent))

    testObserver.assertValueAt(4, MatchesViewState::refreshLoading)
    testObserver.assertValueAt(5) { state -> !state.refreshLoading }

    testObserver.values().let { viewStates ->
      assert(viewStates[5].matches.size < viewStates[4].matches.size) {
        "A refresh should override the list"
      }
    }
  }

  @Test fun loadNextPage_shouldAppendMatches() {
    // load first page
    stateBinder.processIntents(Observable.just(
        MatchesIntent.InitialIntent))
    // load second page
    stateBinder.processIntents(Observable.just(
        MatchesIntent.LoadNextPageIntent(1)))
    testObserver.assertValueAt(2, MatchesViewState::nextPageLoading)
    testObserver.assertValueAt(3) { state -> !state.nextPageLoading }

    testObserver.values().let { viewStates ->
      assert(viewStates[3].matches.size > viewStates[2].matches.size) {
        "A next page loading should append matches"
      }
    }
  }
}