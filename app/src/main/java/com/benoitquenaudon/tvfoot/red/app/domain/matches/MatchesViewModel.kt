package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.data.source.PreferenceRepository
import com.benoitquenaudon.tvfoot.red.app.common.firebase.BaseRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.data.source.BaseMatchesRepository
import com.benoitquenaudon.tvfoot.red.app.data.source.BasePreferenceRepository
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.ClearFiltersAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.LoadNextPageAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.LoadTagsAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.RefreshAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.ToggleFilterAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.ClearFilters
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterInitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.InitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.LoadNextPageIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.RefreshIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.ToggleFilterIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.ClearFiltersResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.LoadNextPageResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.LoadTagsResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.RefreshResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.ToggleFilterResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.mvi.RedViewModel
import com.benoitquenaudon.tvfoot.red.util.MatchId
import com.benoitquenaudon.tvfoot.red.util.WillBeNotified
import com.benoitquenaudon.tvfoot.red.util.flatMapIterable
import com.benoitquenaudon.tvfoot.red.util.logAction
import com.benoitquenaudon.tvfoot.red.util.logIntent
import com.benoitquenaudon.tvfoot.red.util.logResult
import com.benoitquenaudon.tvfoot.red.util.logState
import com.benoitquenaudon.tvfoot.red.util.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.toMap
import io.reactivex.subjects.PublishSubject
import java.util.ArrayList
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class MatchesViewModel @Inject constructor(
    private val intentsSubject: PublishSubject<MatchesIntent>,
    private val matchesRepository: BaseMatchesRepository,
    private val preferenceRepository: BasePreferenceRepository,
    private val schedulerProvider: BaseSchedulerProvider,
    firebaseAnalytics: BaseRedFirebaseAnalytics
) : RedViewModel<MatchesIntent, MatchesViewState>(firebaseAnalytics) {

  private val statesObservable: Observable<MatchesViewState> by lazy(NONE) {
    compose().skip(1).replay(1).autoConnect(0)
  }

  override fun states(): Observable<MatchesViewState> = statesObservable

  override fun processIntents(intents: Observable<MatchesIntent>) {
    intents.subscribe(intentsSubject::onNext)
  }

  private fun compose(): Observable<MatchesViewState> {
    return intentsSubject
        .publish { shared ->
          Observable.merge(
              shared.ofType(InitialIntent::class.java).take(1),
              shared.notOfType(InitialIntent::class.java)
          )
        }
        .publish { shared ->
          Observable.merge(
              shared.ofType(FilterInitialIntent::class.java).take(1),
              shared.notOfType(FilterInitialIntent::class.java)
          )
        }
        .doOnNext(this::logIntent)
        .map(this::actionFromIntent)
        .doOnNext(this::logAction)
        .compose<MatchesResult>(actionToResultTransformer)
        .doOnNext(this::logResult)
        .scan(MatchesViewState.idle(), reducer)
        .doOnNext(this::logState)
  }

  private fun actionFromIntent(intent: MatchesIntent): MatchesAction =
      when (intent) {
        is InitialIntent -> RefreshAction
        is RefreshIntent -> RefreshAction
        is LoadNextPageIntent -> LoadNextPageAction(intent.pageIndex)
        is ClearFilters -> ClearFiltersAction
        is ToggleFilterIntent -> ToggleFilterAction(intent.tagName)
        is FilterInitialIntent -> LoadTagsAction
      }

  private val refreshTransformer: ObservableTransformer<RefreshAction, RefreshResult>
    get() = ObservableTransformer { actions: Observable<RefreshAction> ->
      actions.flatMap({
        val matches: Observable<Match> = matchesRepository
            .loadPage(0)
            .flatMapIterable()
            .share()

        Single.zip(
            matches.toList(),
            matches.map(Match::id)
                .flatMapSingle { matchId ->
                  preferenceRepository
                      .loadNotifyMatchStart(matchId)
                      .map { matchId to it }
                }.toMap(),
            BiFunction<List<Match>,
                Map<String, WillBeNotified>,
                Pair<List<Match>, Map<MatchId, WillBeNotified>>>(::Pair)
        )
            .toObservable()
            .map<RefreshResult> { RefreshResult.Success(it.first, it.second) }
            .onErrorReturn(RefreshResult::Failure)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .startWith(RefreshResult.InFlight)
      })
    }

  private
  val loadNextPageTransformer: ObservableTransformer<LoadNextPageAction, LoadNextPageResult>
    get () = ObservableTransformer { actions: Observable<LoadNextPageAction> ->
      actions.flatMap(
          { (pageIndex) ->
            val matches: Observable<Match> = matchesRepository
                .loadPage(pageIndex)
                .flatMapIterable()
                .share()

            Single.zip(
                matches.toList(),
                matches.map(Match::id)
                    .flatMapSingle { matchId ->
                      preferenceRepository
                          .loadNotifyMatchStart(matchId)
                          .map { matchId to it }
                    }.toMap(),
                BiFunction<List<Match>,
                    Map<String, WillBeNotified>,
                    Pair<List<Match>, Map<MatchId, WillBeNotified>>>(::Pair)
            )
                .toObservable()
                .map<LoadNextPageResult> { (matches, notificationPairs) ->
                  LoadNextPageResult.Success(pageIndex, matches, notificationPairs)
                }
                .onErrorReturn(LoadNextPageResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(LoadNextPageResult.InFlight)
          })
    }

  private
  val loadTagsTransformer: ObservableTransformer<LoadTagsAction, LoadTagsResult>
    get () = ObservableTransformer { actions: Observable<LoadTagsAction> ->
      actions.flatMap(
          {
            matchesRepository.loadTags()
                .toObservable()
                .map<LoadTagsResult>(LoadTagsResult::Success)
                .onErrorReturn(LoadTagsResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(LoadTagsResult.InFlight)
          })
    }

  private
  val clearFilterTransformer: ObservableTransformer<ClearFiltersAction, ClearFiltersResult>
    get () = ObservableTransformer { actions: Observable<ClearFiltersAction> ->
      actions.map({ ClearFiltersResult })
    }

  private
  val toggleFilterTransformer: ObservableTransformer<ToggleFilterAction, ToggleFilterResult>
    get () = ObservableTransformer { actions: Observable<ToggleFilterAction> ->
      actions.map({ ToggleFilterResult(it.tagName) })
    }

  private
  val actionToResultTransformer: ObservableTransformer<MatchesAction, MatchesResult>
    get () = ObservableTransformer { actions: Observable<MatchesAction> ->
      actions.publish({ shared ->
        Observable.merge<MatchesResult>(
            shared.ofType(RefreshAction::class.java).compose(refreshTransformer),
            shared.ofType(LoadNextPageAction::class.java).compose(loadNextPageTransformer))
            .mergeWith(
                Observable.merge<MatchesResult>(
                    shared.ofType(ClearFiltersAction::class.java).compose(clearFilterTransformer),
                    shared.ofType(ToggleFilterAction::class.java).compose(
                        toggleFilterTransformer),
                    shared.ofType(LoadTagsAction::class.java).compose(loadTagsTransformer))
            )
            .mergeWith(
                // Error for not implemented actions
                shared.filter({ v ->
                  v !is RefreshAction &&
                      v !is LoadNextPageAction &&
                      v !is ClearFiltersAction &&
                      v !is ToggleFilterAction &&
                      v !is LoadTagsAction
                }).flatMap({ w ->
                  Observable.error<MatchesResult>(
                      IllegalArgumentException("Unknown Action type: " + w))
                }))
      })
    }

  companion object Reducer {

    private val reducer = BiFunction { previousState: MatchesViewState, result: MatchesResult ->
      when (result) {
        is RefreshResult -> {
          when (result) {
            is RefreshResult.InFlight ->
              previousState.copy(refreshLoading = true, error = null)
            is RefreshResult.Failure ->
              previousState.copy(refreshLoading = false, error = result.throwable)
            is RefreshResult.Success -> {
              val matches = checkNotNull(result.matches) { "Matches is null" }
              val willBeNotifiedPairs = checkNotNull(result.willBeNotifiedPairs)

              previousState.copy(
                  refreshLoading = false,
                  error = null,
                  hasMore = !matches.isEmpty(),
                  currentPage = 0,
                  matches = MatchRowDisplayable.fromMatches(matches, willBeNotifiedPairs))
            }
          }
        }
        is LoadNextPageResult -> {
          when (result) {
            is LoadNextPageResult.InFlight ->
              previousState.copy(nextPageLoading = true, error = null)
            is LoadNextPageResult.Failure ->
              previousState.copy(nextPageLoading = false, error = result.throwable)
            is LoadNextPageResult.Success -> {
              val newMatches = checkNotNull(result.matches) { "Matches are null" }
              val willBeNotifiedPairs = checkNotNull(result.willBeNotifiedPairs)

              val matches = ArrayList<MatchRowDisplayable>()
              matches.addAll(previousState.matches)
              matches.addAll(MatchRowDisplayable.fromMatches(newMatches, willBeNotifiedPairs))

              previousState.copy(
                  nextPageLoading = false,
                  error = null,
                  matches = matches,
                  currentPage = result.pageIndex,
                  hasMore = !newMatches.isEmpty())
            }
          }
        }
        is ClearFiltersResult -> previousState.copy(filteredTags = emptyMap())
        is ToggleFilterResult -> {
          previousState.filteredTags.toMutableMap().let {
            if (it.keys.contains(result.tagName)) {
              it.remove(result.tagName)
            } else {
              it.put(result.tagName,
                  previousState.tags.first { it.name == result.tagName }.targets)
            }
            if (it.isEmpty()) {
              previousState.copy(filteredTags = it, hasMore = true)
            } else {
              previousState.copy(filteredTags = it)
            }
          }
        }
        is LoadTagsResult -> {
          when (result) {
            is LoadTagsResult.InFlight ->
              previousState.copy(tagsLoading = true, tagsError = null)
            is LoadTagsResult.Failure ->
              previousState.copy(tagsLoading = false, tagsError = result.throwable)
            is LoadTagsResult.Success ->
              previousState.copy(
                  tagsLoading = false,
                  tags = result.tags.filter(Tag::display)
              )
          }
        }
      }
    }
  }
}