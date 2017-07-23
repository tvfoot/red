package com.benoitquenaudon.tvfoot.red.app.domain.match.state

import com.benoitquenaudon.tvfoot.red.app.common.PreferenceRepository
import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification
import com.benoitquenaudon.tvfoot.red.app.common.firebase.BaseRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.notification.NotificationRepository
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchAction.GetLastStateAction
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchAction.LoadMatchAction
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchAction.NotifyMatchStartAction
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchIntent.GetLastState
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchIntent.InitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchIntent.NotifyMatchStartIntent
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchResult.GetLastStateResult
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchResult.LoadMatchResult
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchResult.NotifyMatchStartResult
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchResult.Status.LOAD_MATCH_FAILURE
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchResult.Status.LOAD_MATCH_IN_FLIGHT
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchResult.Status.LOAD_MATCH_SUCCESS
import com.benoitquenaudon.tvfoot.red.app.injection.scope.ScreenScope
import com.benoitquenaudon.tvfoot.red.app.mvi.StateBinder
import com.benoitquenaudon.tvfoot.red.util.logAction
import com.benoitquenaudon.tvfoot.red.util.logIntent
import com.benoitquenaudon.tvfoot.red.util.logResult
import com.benoitquenaudon.tvfoot.red.util.logState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@ScreenScope class MatchStateBinder @Inject constructor(
    private val intentsSubject: PublishSubject<MatchIntent>,
    private val statesSubject: PublishSubject<MatchViewState>,
    private val matchRepository: MatchRepository,
    private val preferenceRepository: PreferenceRepository,
    private val notificationRepository: NotificationRepository,
    private val schedulerProvider: BaseSchedulerProvider,
    firebaseAnalytics: BaseRedFirebaseAnalytics
) : StateBinder(firebaseAnalytics) {

  init {
    compose().subscribe(statesSubject::onNext)
  }

  fun forwardIntents(intents: Observable<MatchIntent>) {
    intents.subscribe(intentsSubject::onNext)
  }

  fun statesAsObservable(): Observable<MatchViewState> = statesSubject

  private fun compose(): Observable<MatchViewState> {
    return intentsSubject
        .doOnNext(this::logIntent)
        .scan(initialIntentFilter)
        .map(this::actionFromIntent)
        .doOnNext(this::logAction)
        .compose<MatchResult>(actionToResultTransformer)
        .doOnNext(this::logResult)
        .scan(MatchViewState.idle(), reducer)
        .doOnNext(this::logState)
  }

  private val initialIntentFilter: BiFunction<MatchIntent, MatchIntent, MatchIntent>
    get() = BiFunction { _: MatchIntent, newIntent: MatchIntent ->
      // if isReConnection (e.g. after config change)
      // i.e. we are inside the scan, meaning there has already
      // been intent in the past, meaning the InitialIntent cannot
      // be the first => it is a reconnection.
      if (newIntent is InitialIntent) {
        GetLastState
      } else {
        newIntent
      }
    }

  private fun actionFromIntent(intent: MatchIntent): MatchAction =
      when (intent) {
        is InitialIntent -> LoadMatchAction(intent.matchId)
        is GetLastState -> GetLastStateAction
        is NotifyMatchStartIntent ->
          NotifyMatchStartAction(intent.matchId, intent.startAt, intent.notifyMatchStart)
      }

  private val loadMatchTransformer: ObservableTransformer<LoadMatchAction, LoadMatchResult>
    get() = ObservableTransformer { actions: Observable<LoadMatchAction> ->
      actions.flatMap({ (matchId) ->
        Single.zip<Match, Boolean, LoadMatchResult>(matchRepository.loadMatch(matchId),
            preferenceRepository.loadNotifyMatchStart(matchId),
            BiFunction<Match, Boolean, LoadMatchResult> { match, shouldNotifyMatchStart ->
              LoadMatchResult.success(match, shouldNotifyMatchStart)
            })
            .toObservable()
            .onErrorReturn({ LoadMatchResult.failure(it) })
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .startWith(LoadMatchResult.inFlight())
      })
    }

  private val notifyMatchStartTransformer: ObservableTransformer<NotifyMatchStartAction, NotifyMatchStartResult>
    get() = ObservableTransformer { actions: Observable<NotifyMatchStartAction> ->
      actions.flatMap({ (matchId, startAt, notifyMatchStart) ->
        preferenceRepository.saveNotifyMatchStart(matchId, notifyMatchStart)
            .flatMap<StreamNotification> {
              notificationRepository.scheduleNotification(matchId, startAt, notifyMatchStart)
            }
            .toObservable()
            .map { NotifyMatchStartResult(notifyMatchStart) }
      })
    }

  private val getLastStateTransformer: ObservableTransformer<GetLastStateAction, GetLastStateResult>
    get() = ObservableTransformer { actions: Observable<GetLastStateAction> ->
      actions.map({ GetLastStateResult })
    }

  private val actionToResultTransformer: ObservableTransformer<MatchAction, MatchResult>
    get() = ObservableTransformer { actions: Observable<MatchAction> ->
      actions.publish({ shared ->
        Observable.merge<MatchResult>(
            shared.ofType(LoadMatchAction::class.java).compose(loadMatchTransformer),
            shared.ofType(GetLastStateAction::class.java).compose(getLastStateTransformer),
            shared.ofType(NotifyMatchStartAction::class.java).compose(notifyMatchStartTransformer))
            .mergeWith(
                // Error for not implemented actions
                shared.filter({ v ->
                  v !is LoadMatchAction && v !is GetLastStateAction && v !is NotifyMatchStartAction
                }).flatMap({ w ->
                  Observable.error<MatchResult>(
                      IllegalArgumentException("Unknown Action type: " + w))
                }))
      })
    }

  companion object {
    private val reducer = BiFunction { previousState: MatchViewState, matchResult: MatchResult ->
      when (matchResult) {
        is LoadMatchResult -> {
          when (matchResult.status) {
            LOAD_MATCH_IN_FLIGHT -> previousState.copy(loading = true, error = null)
            LOAD_MATCH_FAILURE -> {
              previousState.copy(loading = false, error = matchResult.error)
            }
            LOAD_MATCH_SUCCESS -> {
              val match: Match = checkNotNull(matchResult.match) { "Match == null" }

              previousState.copy(
                  loading = false,
                  error = null,
                  shouldNotifyMatchStart = matchResult.shouldNotifyMatchStart,
                  match = MatchDisplayable.fromMatch(match))
            }
          }
        }
        is GetLastStateResult -> previousState.copy()
        is NotifyMatchStartResult -> {
          previousState.copy(shouldNotifyMatchStart = matchResult.shouldNotifyMatchStart)
        }
      }
    }
  }
}
