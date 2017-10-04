package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.common.firebase.BaseRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.data.source.BaseMatchesRepository
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.ClearFiltersAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.GetLastStateAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.LoadNextPageAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.LoadTagsAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.RefreshAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.ToggleFilterAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.ClearFilters
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterInitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.GetLastState
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.InitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.LoadNextPageIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.RefreshIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.ToggleFilterIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.ClearFiltersResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.GetLastStateResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.LoadNextPageResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.LoadTagsResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.RefreshResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.ToggleFilterResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.mvi.RedViewModel
import com.benoitquenaudon.tvfoot.red.util.logAction
import com.benoitquenaudon.tvfoot.red.util.logIntent
import com.benoitquenaudon.tvfoot.red.util.logResult
import com.benoitquenaudon.tvfoot.red.util.logState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import java.util.ArrayList
import javax.inject.Inject

class MatchesViewModel @Inject constructor(
    private val intentsSubject: PublishSubject<MatchesIntent>,
    private val statesSubject: PublishSubject<MatchesViewState>,
    private val repository: BaseMatchesRepository,
    private val schedulerProvider: BaseSchedulerProvider,
    firebaseAnalytics: BaseRedFirebaseAnalytics
) : RedViewModel<MatchesIntent, MatchesViewState>(firebaseAnalytics) {

  init {
    compose().subscribe(statesSubject::onNext)
  }

  override fun processIntents(intents: Observable<MatchesIntent>) {
    intents.subscribe(intentsSubject::onNext)
  }

  override fun states(): Observable<MatchesViewState> = statesSubject

  private fun compose(): Observable<MatchesViewState> {
    return intentsSubject
        .doOnNext(this::logIntent)
        .scan(initialIntentFilter)
        .map(this::actionFromIntent)
        .doOnNext(this::logAction)
        .compose<MatchesResult>(actionToResultTransformer)
        .doOnNext(this::logResult)
        .scan(MatchesViewState.idle(), reducer)
        .doOnNext(this::logState)
  }

  private val initialIntentFilter: BiFunction<MatchesIntent, MatchesIntent, MatchesIntent>
    get() = BiFunction { _: MatchesIntent, newIntent: MatchesIntent ->
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

  private fun actionFromIntent(intent: MatchesIntent): MatchesAction =
      when (intent) {
        is InitialIntent -> RefreshAction
        is RefreshIntent -> RefreshAction
        is GetLastState -> GetLastStateAction
        is LoadNextPageIntent -> LoadNextPageAction(intent.pageIndex)
        is ClearFilters -> ClearFiltersAction
        is ToggleFilterIntent -> ToggleFilterAction(intent.tagName)
        is FilterInitialIntent -> LoadTagsAction
      }

  private val refreshTransformer: ObservableTransformer<RefreshAction, RefreshResult>
    get() = ObservableTransformer { actions: Observable<RefreshAction> ->
      actions.flatMap({
        repository.loadPage(0)
            .toObservable()
            .map<RefreshResult>(RefreshResult::Success)
            .onErrorReturn(RefreshResult::Failure)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .startWith(RefreshResult.InFlight)
      })
    }

  private val loadNextPageTransformer: ObservableTransformer<LoadNextPageAction, LoadNextPageResult>
    get() = ObservableTransformer { actions: Observable<LoadNextPageAction> ->
      actions.flatMap(
          { (pageIndex) ->
            repository.loadPage(pageIndex)
                .toObservable()
                .map<LoadNextPageResult> { matches ->
                  LoadNextPageResult.Success(pageIndex, matches)
                }
                .onErrorReturn(LoadNextPageResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(LoadNextPageResult.InFlight)
          })
    }

  private val loadTagsTransformer: ObservableTransformer<LoadTagsAction, LoadTagsResult>
    get() = ObservableTransformer { actions: Observable<LoadTagsAction> ->
      actions.flatMap(
          {
            repository.loadTags()
                .toObservable()
                .map<LoadTagsResult>(LoadTagsResult::Success)
                .onErrorReturn(LoadTagsResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(LoadTagsResult.InFlight)
          })
    }

  private val getLastStateTransformer: ObservableTransformer<GetLastStateAction, GetLastStateResult>
    get() = ObservableTransformer { actions: Observable<GetLastStateAction> ->
      actions.map({ GetLastStateResult })
    }

  private val clearFilterTransformer: ObservableTransformer<ClearFiltersAction, ClearFiltersResult>
    get() = ObservableTransformer { actions: Observable<ClearFiltersAction> ->
      actions.map({ ClearFiltersResult })
    }

  private val toggleFilterTransformer: ObservableTransformer<ToggleFilterAction, ToggleFilterResult>
    get() = ObservableTransformer { actions: Observable<ToggleFilterAction> ->
      actions.map({ ToggleFilterResult(it.tagName) })
    }

  private val actionToResultTransformer: ObservableTransformer<MatchesAction, MatchesResult>
    get() = ObservableTransformer { actions: Observable<MatchesAction> ->
      actions.publish({ shared ->
        Observable.merge<MatchesResult>(
            shared.ofType(RefreshAction::class.java).compose(refreshTransformer),
            shared.ofType(LoadNextPageAction::class.java).compose(loadNextPageTransformer),
            shared.ofType(GetLastStateAction::class.java).compose(getLastStateTransformer))
            .mergeWith(
                Observable.merge<MatchesResult>(
                    shared.ofType(ClearFiltersAction::class.java).compose(clearFilterTransformer),
                    shared.ofType(ToggleFilterAction::class.java).compose(toggleFilterTransformer),
                    shared.ofType(LoadTagsAction::class.java).compose(loadTagsTransformer))
            )
            .mergeWith(
                // Error for not implemented actions
                shared.filter({ v ->
                  v !is RefreshAction &&
                      v !is LoadNextPageAction &&
                      v !is GetLastStateAction &&
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
              val matches = checkNotNull((result).matches) { "Matches are null" }

              previousState.copy(
                  refreshLoading = false,
                  error = null,
                  hasMore = !matches.isEmpty(),
                  currentPage = 0,
                  matches = MatchRowDisplayable.fromMatches(matches))
            }
          }
        }
        is GetLastStateResult -> previousState.copy()
        is LoadNextPageResult -> {
          when (result) {
            is LoadNextPageResult.InFlight ->
              previousState.copy(nextPageLoading = true, error = null)
            is LoadNextPageResult.Failure ->
              previousState.copy(nextPageLoading = false, error = result.throwable)
            is LoadNextPageResult.Success -> {
              val newMatches = checkNotNull((result).matches) { "Matches are null" }

              val matches = ArrayList<MatchRowDisplayable>()
              matches.addAll(previousState.matches)
              matches.addAll(MatchRowDisplayable.fromMatches(newMatches))

              previousState.copy(
                  nextPageLoading = false,
                  error = null,
                  matches = matches,
                  currentPage = (result).pageIndex,
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
              it.put(result.tagName, previousState.tags.first { it.name == result.tagName }.targets)
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