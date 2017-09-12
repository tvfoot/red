package com.benoitquenaudon.tvfoot.red.app.domain.matches.state

import com.benoitquenaudon.tvfoot.red.app.common.LceStatus.FAILURE
import com.benoitquenaudon.tvfoot.red.app.common.LceStatus.IN_FLIGHT
import com.benoitquenaudon.tvfoot.red.app.common.LceStatus.SUCCESS
import com.benoitquenaudon.tvfoot.red.app.common.firebase.BaseRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.data.source.BaseMatchesRepository
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesAction.ClearFiltersAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesAction.GetLastStateAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesAction.LoadNextPageAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesAction.LoadTagsAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesAction.RefreshAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesAction.ToggleFilterAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent.ClearFilters
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent.FilterInitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent.GetLastState
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent.InitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent.LoadNextPageIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent.RefreshIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent.ToggleFilterIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.ClearFiltersResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.GetLastStateResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.LoadNextPageResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.LoadTagsResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.LoadTagsResult.Factory
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.RefreshResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesResult.ToggleFilterResult
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
            .map(RefreshResult.Factory::success)
            .onErrorReturn(RefreshResult.Factory::failure)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .startWith(RefreshResult.inFlight())
      })
    }

  private val loadNextPageTransformer: ObservableTransformer<LoadNextPageAction, LoadNextPageResult>
    get() = ObservableTransformer { actions: Observable<LoadNextPageAction> ->
      actions.flatMap(
          { (pageIndex) ->
            repository.loadPage(pageIndex)
                .toObservable()
                .map { matches -> LoadNextPageResult.success(pageIndex, matches) }
                .onErrorReturn(LoadNextPageResult.Factory::failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(LoadNextPageResult.inFlight())
          })
    }

  private val loadTagsTransformer: ObservableTransformer<LoadTagsAction, LoadTagsResult>
    get() = ObservableTransformer { actions: Observable<LoadTagsAction> ->
      actions.flatMap(
          {
            repository.loadTags()
                .toObservable()
                .map(Factory::success)
                .onErrorReturn(Factory::failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(LoadTagsResult.inFlight())
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

    private val reducer = BiFunction { previousState: MatchesViewState, matchesResult: MatchesResult ->
      when (matchesResult) {
        is RefreshResult -> {
          when (matchesResult.status) {
            IN_FLIGHT -> previousState.copy(refreshLoading = true, error = null)
            FAILURE -> previousState.copy(refreshLoading = false, error = matchesResult.throwable)
            SUCCESS -> {
              val matches = checkNotNull((matchesResult).matches) { "Matches are null" }

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
          when (matchesResult.status) {
            IN_FLIGHT -> previousState.copy(nextPageLoading = true, error = null)
            FAILURE -> previousState.copy(nextPageLoading = false, error = matchesResult.error)
            SUCCESS -> {
              val newMatches = checkNotNull((matchesResult).matches) { "Matches are null" }

              val matches = ArrayList<MatchRowDisplayable>()
              matches.addAll(previousState.matches)
              matches.addAll(MatchRowDisplayable.fromMatches(newMatches))

              previousState.copy(
                  nextPageLoading = false,
                  error = null,
                  matches = matches,
                  currentPage = (matchesResult).pageIndex,
                  hasMore = !newMatches.isEmpty())
            }
          }
        }
        is MatchesResult.ClearFiltersResult -> previousState.copy(filteredTags = emptyMap())
        is MatchesResult.ToggleFilterResult -> {
          previousState.filteredTags.toMutableMap().let {
            if (it.keys.contains(matchesResult.tagName)) {
              it.remove(matchesResult.tagName)
            } else {
              it.put(matchesResult.tagName,
                  previousState.tags.first { it.name == matchesResult.tagName }.targets)
            }
            previousState.copy(filteredTags = it)
          }
        }
        is MatchesResult.LoadTagsResult -> {
          when (matchesResult.status) {
            IN_FLIGHT -> previousState.copy(tagsLoading = true, tagsError = null)
            FAILURE -> previousState.copy(tagsLoading = false, tagsError = matchesResult.error)
            SUCCESS -> {
              checkNotNull(matchesResult.tags).let { tags ->
                previousState.copy(tagsLoading = false, tags = tags.filter(Tag::display))
              }
            }
          }
        }
      }
    }
  }
}
