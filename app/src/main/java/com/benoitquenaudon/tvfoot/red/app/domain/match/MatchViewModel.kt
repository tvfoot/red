package com.benoitquenaudon.tvfoot.red.app.domain.match

import com.benoitquenaudon.tvfoot.red.app.common.StreamNotification
import com.benoitquenaudon.tvfoot.red.app.common.firebase.BaseRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.notification.NotificationRepository
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.source.BaseMatchRepository
import com.benoitquenaudon.tvfoot.red.app.data.source.PreferenceRepository
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchAction.LoadMatchAction
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchAction.NotifyMatchStartAction
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchIntent.InitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchIntent.NotifyMatchStartIntent
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchResult.LoadMatchResult
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchResult.NotifyMatchStartResult
import com.benoitquenaudon.tvfoot.red.app.mvi.RedViewModel
import com.benoitquenaudon.tvfoot.red.util.logAction
import com.benoitquenaudon.tvfoot.red.util.logIntent
import com.benoitquenaudon.tvfoot.red.util.logResult
import com.benoitquenaudon.tvfoot.red.util.logState
import com.benoitquenaudon.tvfoot.red.util.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class MatchViewModel @Inject constructor(
    private val intentsSubject: PublishSubject<MatchIntent>,
    private val matchRepository: BaseMatchRepository,
    private val preferenceRepository: PreferenceRepository,
    private val notificationRepository: NotificationRepository,
    private val schedulerProvider: BaseSchedulerProvider,
    firebaseAnalytics: BaseRedFirebaseAnalytics
) : RedViewModel<MatchIntent, MatchViewState>(firebaseAnalytics) {

  private val statesObservable: Observable<MatchViewState> by lazy(NONE) {
    compose().skip(1).replay(1).autoConnect(0)
  }

  override fun processIntents(intents: Observable<MatchIntent>) {
    intents.subscribe(intentsSubject::onNext)
  }

  override fun states(): Observable<MatchViewState> = statesObservable

  private fun compose(): Observable<MatchViewState> {
    return intentsSubject
        .publish { shared ->
          Observable.merge(
              shared.ofType(InitialIntent::class.java).take(1),
              shared.notOfType(InitialIntent::class.java)
          )
        }
        .doOnNext(this::logIntent)
        .map(this::actionFromIntent)
        .doOnNext(this::logAction)
        .compose<MatchResult>(actionToResultTransformer)
        .doOnNext(this::logResult)
        .scan(MatchViewState.idle(),
            reducer)
        .doOnNext(this::logState)
  }

  private fun actionFromIntent(intent: MatchIntent): MatchAction =
      when (intent) {
        is InitialIntent -> LoadMatchAction(intent.matchId)
        is NotifyMatchStartIntent ->
          NotifyMatchStartAction(intent.matchId, intent.startAt, intent.notifyMatchStart)
      }

  private val loadMatchTransformer: ObservableTransformer<LoadMatchAction, LoadMatchResult>
    get() = ObservableTransformer { actions: Observable<LoadMatchAction> ->
      actions.flatMap({ (matchId) ->
        Single.zip<Match, Boolean, LoadMatchResult>(matchRepository.loadMatch(matchId),
            preferenceRepository.loadNotifyMatchStart(matchId),
            BiFunction<Match, Boolean, LoadMatchResult> { match, shouldNotifyMatchStart ->
              LoadMatchResult.Success(match, shouldNotifyMatchStart)
            })
            .toObservable()
            .onErrorReturn(LoadMatchResult::Failure)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .startWith(LoadMatchResult.InFlight)
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

  private val actionToResultTransformer: ObservableTransformer<MatchAction, MatchResult>
    get() = ObservableTransformer { actions: Observable<MatchAction> ->
      actions.publish({ shared ->
        Observable.merge<MatchResult>(
            shared.ofType(LoadMatchAction::class.java).compose(loadMatchTransformer),
            shared.ofType(NotifyMatchStartAction::class.java).compose(notifyMatchStartTransformer))
            .mergeWith(
                // Error for not implemented actions
                shared.filter({ v ->
                  v !is LoadMatchAction && v !is NotifyMatchStartAction
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
          when (matchResult) {
            is LoadMatchResult.InFlight ->
              previousState.copy(loading = true, error = null)
            is LoadMatchResult.Failure ->
              previousState.copy(loading = false, error = matchResult.throwable)
            is LoadMatchResult.Success -> {
              val match: Match = checkNotNull(matchResult.match) { "Match == null" }

              previousState.copy(
                  loading = false,
                  error = null,
                  shouldNotifyMatchStart = matchResult.shouldNotifyMatchStart,
                  match = MatchDisplayable.fromMatch(match))
            }
          }
        }
        is NotifyMatchStartResult -> {
          previousState.copy(shouldNotifyMatchStart = matchResult.shouldNotifyMatchStart)
        }
      }
    }
  }
}
