package io.oldering.tvfoot.red.match;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.RedAppConfig;
import io.oldering.tvfoot.red.databinding.ActivityMatchBinding;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import io.oldering.tvfoot.red.match.state.MatchIntent;
import io.oldering.tvfoot.red.match.state.MatchStateBinder;
import io.oldering.tvfoot.red.match.state.MatchViewState;
import io.oldering.tvfoot.red.matches.BroadcastersAdapter;
import io.oldering.tvfoot.red.matches.displayable.BroadcasterRowDisplayable;
import io.oldering.tvfoot.red.util.BaseActivity;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static io.oldering.tvfoot.red.util.Preconditions.checkNotNull;

public class MatchActivity extends BaseActivity {
  @Inject FlowController flowController;
  @Inject MatchStateBinder stateBinder;
  @Inject CompositeDisposable disposables;

  private ActivityMatchBinding binding;
  @Nullable private String matchId = null;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivityComponent().inject(this);

    final Uri uri = getIntent().getData();
    if (uri != null && //
        RedAppConfig.AUTHORITIES.contains(uri.getAuthority()) && //
        RedAppConfig.SCHEMES.contains(uri.getScheme())) {
      final List<String> segments = uri.getPathSegments();
      if (segments != null && //
          segments.size() == 5 && //
          RedAppConfig.PATH_MATCH.equals(segments.get(0))) {
        matchId = segments.get(4);
      }
    }

    if (matchId == null) {
      Timber.w("match id is null %s", uri);
      Toast.makeText(this, "match id is null with uri " + uri, Toast.LENGTH_LONG).show();
      flowController.toMatches();
      finish();
      return;
    }

    binding = DataBindingUtil.setContentView(this, R.layout.activity_match);

    Timber.d("match with load with id %s", matchId);
    disposables = new CompositeDisposable();
    bind();
  }

  private void bind() {
    disposables.add(stateBinder.getStatesAsObservable().subscribe(this::render));
    stateBinder.forwardIntents(intents());
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    disposables.dispose();
  }

  public Observable<MatchIntent> intents() {
    return initialIntent();
  }

  private Observable<MatchIntent> initialIntent() {
    return Observable.just(
        MatchIntent.InitialIntent.create(checkNotNull(matchId, "MatchId is null")));
  }

  public void render(MatchViewState state) {
    switch (state.status()) {
      case LOAD_MATCH_IN_FLIGHT:
        renderMatchLoading();
        break;
      case LOAD_MATCH_FAILURE:
        renderError();
        break;
      case LOAD_MATCH_SUCCESS:
        renderMatchLoaded(state);
        break;
    }
  }

  private void renderMatchLoading() {
    binding.matchContainer.setVisibility(GONE);
    binding.errorView.setVisibility(GONE);
    binding.progressBar.setVisibility(VISIBLE);
  }

  private void renderError() {
    binding.matchContainer.setVisibility(GONE);
    binding.errorView.setVisibility(VISIBLE);
    binding.progressBar.setVisibility(GONE);
  }

  private void renderMatchLoaded(MatchViewState state) {
    MatchDisplayable match =
        checkNotNull(state.match(), "match == null for renderMatchLoaded with state " + state);
    binding.setMatch(match);
    setupBroadcastersView(match.broadcasters());

    binding.matchContainer.setVisibility(VISIBLE);
    binding.errorView.setVisibility(GONE);
    binding.progressBar.setVisibility(GONE);
  }

  private void setupBroadcastersView(List<BroadcasterRowDisplayable> broadcasters) {
    BroadcastersAdapter broadcastersAdapter = new BroadcastersAdapter();
    broadcastersAdapter.addAll(broadcasters);
    binding.matchDetailBroadcasters.setAdapter(broadcastersAdapter);
  }
}
