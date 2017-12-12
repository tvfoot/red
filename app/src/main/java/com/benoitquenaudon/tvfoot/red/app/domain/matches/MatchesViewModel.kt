package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.common.firebase.BaseRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.data.source.BaseMatchesRepository
import com.benoitquenaudon.tvfoot.red.app.data.source.BasePreferenceRepository
import com.benoitquenaudon.tvfoot.red.app.data.source.BaseTeamRepository
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.FilterAction.ClearFiltersAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.FilterAction.ClearSearchInputAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.FilterAction.LoadTagsAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.FilterAction.SearchInputAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.FilterAction.SearchInputAction.ClearSearchAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.FilterAction.SearchInputAction.SearchTeamAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.FilterAction.SearchedTeamSelectedAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.FilterAction.ToggleFilterCompetitionAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.FilterAction.ToggleFilterTeamAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.LoadNextPageAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.RefreshAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesAction.RefreshNotificationStatusAction
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.ClearFilters
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.ClearSearchInputIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.FilterInitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.SearchInputIntent.ClearSearchIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.SearchInputIntent.SearchTeamIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.SearchedTeamSelectedIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.ToggleFilterIntent.ToggleFilterCompetitionIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.FilterIntent.ToggleFilterIntent.ToggleFilterTeamIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.InitialIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.LoadNextPageIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.RefreshIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent.RefreshNotificationStatusIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.ClearFiltersResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.ClearSearchInputResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.LoadTagsResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchInputResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchInputResult.ClearSearchResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchInputResult.SearchTeamResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchedTeamSelectedResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchedTeamSelectedResult.TeamSearchFailure
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchedTeamSelectedResult.TeamSearchInFlight
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchedTeamSelectedResult.TeamSearchSuccess
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.ToggleFilterCompetitionResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.ToggleFilterTeamResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.LoadNextPageResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.RefreshNotificationStatusResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.RefreshResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchResultDisplayable
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
    private val teamRepository: BaseTeamRepository,
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
        is ToggleFilterCompetitionIntent -> ToggleFilterCompetitionAction(intent.tagName)
        is ToggleFilterTeamIntent -> ToggleFilterTeamAction(intent.teamCode)
        is FilterInitialIntent -> LoadTagsAction
        is SearchTeamIntent -> SearchTeamAction(intent.input)
        is ClearSearchIntent -> ClearSearchAction
        is SearchedTeamSelectedIntent -> SearchedTeamSelectedAction(intent.team)
        is ClearSearchInputIntent -> ClearSearchInputAction
        is RefreshNotificationStatusIntent -> RefreshNotificationStatusAction(intent.matchId)
      }

  private val refreshTransformer: ObservableTransformer<RefreshAction, RefreshResult>
    get() = ObservableTransformer { actions: Observable<RefreshAction> ->
      actions.flatMap {
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
                Map<MatchId, WillBeNotified>,
                Pair<List<Match>, Map<MatchId, WillBeNotified>>>(::Pair)
        )
            .toObservable()
            .map<RefreshResult> { RefreshResult.Success(it.first, it.second) }
            .onErrorReturn(RefreshResult::Failure)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .startWith(RefreshResult.InFlight)
      }
    }

  private val loadNextPageTransformer: ObservableTransformer<LoadNextPageAction, LoadNextPageResult>
    get() = ObservableTransformer { actions: Observable<LoadNextPageAction> ->
      actions.flatMap { (pageIndex) ->
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
                Map<MatchId, WillBeNotified>,
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
      }
    }

  private val refreshNotificationStatusTransformer: ObservableTransformer<RefreshNotificationStatusAction, RefreshNotificationStatusResult>
    get() = ObservableTransformer { actions: Observable<RefreshNotificationStatusAction> ->
      actions
          .map(RefreshNotificationStatusAction::matchId)
          .flatMapSingle { matchId ->
            preferenceRepository
                .loadNotifyMatchStart(matchId)
                .map { RefreshNotificationStatusResult(matchId, it) }
          }
    }

  private val loadTagsTransformer: ObservableTransformer<LoadTagsAction, LoadTagsResult>
    get() = ObservableTransformer { actions: Observable<LoadTagsAction> ->
      actions.flatMap {
        matchesRepository.loadTags()
            .toObservable()
            .map<LoadTagsResult>(LoadTagsResult::Success)
            .onErrorReturn(LoadTagsResult::Failure)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .startWith(LoadTagsResult.InFlight)
      }
    }

  private val searchInputTransformer: ObservableTransformer<SearchInputAction, SearchInputResult>
    get() = ObservableTransformer { actions: Observable<SearchInputAction> ->
      actions.switchMap { action ->
        when (action) {
          is SearchTeamAction -> {
            if (action.input.length > 2) {
              teamRepository.findTeams(action.input)
                  .toObservable()
                  .map<SearchTeamResult>({ teams -> SearchTeamResult.Success(action.input, teams) })
                  .onErrorReturn(SearchTeamResult::Failure)
                  .subscribeOn(schedulerProvider.io())
                  .observeOn(schedulerProvider.ui())
                  .startWith(SearchTeamResult.InFlight(action.input))
            } else {
              Observable.just(SearchTeamResult.Success(action.input, emptyList()))
            }
          }
          is ClearSearchAction -> Observable.just(ClearSearchResult)
        }
      }
    }

  private val clearFilterTransformer: ObservableTransformer<ClearFiltersAction, ClearFiltersResult>
    get() = ObservableTransformer { actions: Observable<ClearFiltersAction> ->
      actions.map { ClearFiltersResult }
    }

  private val toggleFilterCompetitionTransformer: ObservableTransformer<ToggleFilterCompetitionAction, ToggleFilterCompetitionResult>
    get() = ObservableTransformer { actions: Observable<ToggleFilterCompetitionAction> ->
      actions.map { ToggleFilterCompetitionResult(it.tagName) }
    }

  private val toggleFilterTeamTransformer: ObservableTransformer<ToggleFilterTeamAction, ToggleFilterTeamResult>
    get() = ObservableTransformer { actions: Observable<ToggleFilterTeamAction> ->
      actions.map { ToggleFilterTeamResult(it.teamCode) }
    }

  private val clearSearchInputTransformer: ObservableTransformer<ClearSearchInputAction, ClearSearchInputResult>
    get() = ObservableTransformer { actions: Observable<ClearSearchInputAction> ->
      actions.map { ClearSearchInputResult }
    }

  private val searchedTeamSelectedTransformer: ObservableTransformer<SearchedTeamSelectedAction, SearchedTeamSelectedResult>
    get() = ObservableTransformer { actions: Observable<SearchedTeamSelectedAction> ->
      actions.map<SearchedTeamSelectedResult> { TeamSearchInFlight(it.team) }
    }

  private val actionToResultTransformer: ObservableTransformer<MatchesAction, MatchesResult>
    get() = ObservableTransformer { actions: Observable<MatchesAction> ->
      actions.publish { shared ->
        Observable.merge<MatchesResult>(
            shared.ofType(RefreshAction::class.java).compose(refreshTransformer),
            shared.ofType(LoadNextPageAction::class.java).compose(loadNextPageTransformer),
            shared.ofType(RefreshNotificationStatusAction::class.java)
                .compose(refreshNotificationStatusTransformer)
        ).mergeWith(
            Observable.merge<MatchesResult>(
                shared.ofType(ClearFiltersAction::class.java).compose(clearFilterTransformer),
                shared.ofType(ToggleFilterCompetitionAction::class.java)
                    .compose(toggleFilterCompetitionTransformer),
                shared.ofType(ToggleFilterTeamAction::class.java)
                    .compose(toggleFilterTeamTransformer),
                shared.ofType(LoadTagsAction::class.java).compose(loadTagsTransformer)
            )
        ).mergeWith(
            Observable.merge<MatchesResult>(
                shared.ofType(SearchInputAction::class.java).compose(searchInputTransformer),
                shared.ofType(SearchedTeamSelectedAction::class.java)
                    .compose(searchedTeamSelectedTransformer),
                shared.ofType(ClearSearchInputAction::class.java).compose(
                    clearSearchInputTransformer)
            )
        ).mergeWith(
            // Error for not implemented actions
            shared.filter { v ->
              v !is RefreshAction &&
                  v !is LoadNextPageAction &&
                  v !is ClearFiltersAction &&
                  v !is ToggleFilterCompetitionAction &&
                  v !is ToggleFilterTeamAction &&
                  v !is LoadTagsAction &&
                  v !is SearchTeamAction &&
                  v !is ClearSearchAction &&
                  v !is SearchedTeamSelectedAction &&
                  v !is ClearSearchInputAction &&
                  v !is RefreshNotificationStatusAction
            }.flatMap { w ->
              Observable.error<MatchesResult>(
                  IllegalArgumentException("Unknown Action type: " + w))
            })
      }
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
        is ClearFiltersResult ->
          previousState.copy(filteredTags = emptyMap(), filteredTeams = emptyList())
        is ToggleFilterCompetitionResult -> {
          previousState.filteredTags.toMutableMap().let {
            if (it.keys.contains(result.tagName)) {
              it.remove(result.tagName)
            } else {
              it.put(result.tagName,
                  previousState.tags.first { it.name == result.tagName }.targets)
            }
            // what was I thinking ??
            if (it.isEmpty()) {
              previousState.copy(filteredTags = it, hasMore = true)
            } else {
              previousState.copy(filteredTags = it)
            }
          }
        }
        is ToggleFilterTeamResult -> {
          previousState.filteredTeams.toMutableList().let { filteredTeams ->
            if (filteredTeams.contains(result.teamCode)) {
              filteredTeams.remove(result.teamCode)
            } else {
              filteredTeams.add(result.teamCode)
            }
            previousState.copy(filteredTeams = filteredTeams)
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
        is SearchTeamResult ->
          when (result) {
            is SearchTeamResult.InFlight ->
              previousState.copy(
                  searchingTeam = true,
                  searchInput = result.searchedInput)
            is SearchTeamResult.Failure ->
              previousState.copy(searchingTeam = false, error = result.throwable)
            is SearchTeamResult.Success ->
              previousState.copy(
                  searchingTeam = false,
                  searchedTeams = result.teams.map { TeamSearchResultDisplayable(it) },
                  searchInput = result.searchedInput
              )
          }
        is ClearSearchResult ->
          previousState.copy(
              searchingTeam = false,
              searchedTeams = emptyList())
        is ClearSearchInputResult -> previousState.copy()
        is SearchedTeamSelectedResult -> when (result) {
          is TeamSearchFailure -> TODO()
          is TeamSearchSuccess -> TODO()
          // TODO(benoit) need to set some new boolean to true
          is TeamSearchInFlight -> previousState.copy(
              teams = listOf(result.team) + previousState.teams,
              filteredTeams = previousState.filteredTeams + result.team.code,
              searchedTeams = emptyList(),
              searchInput = ""
          )
        }
        is RefreshNotificationStatusResult -> {
          val newMatches = previousState.matches.map { match ->
            if (match.matchId == result.matchId) {
              match.copy(willBeNotified = result.willBeNotified)
            } else {
              match
            }
          }
          previousState.copy(matches = newMatches)
        }
      }
    }
  }
}