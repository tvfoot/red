package io.oldering.tvfoot.red.match;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import dagger.android.AndroidInjection;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.RedAppConfig;
import io.oldering.tvfoot.red.databinding.ActivityMatchBinding;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import io.oldering.tvfoot.red.matches.BroadcastersAdapter;
import io.oldering.tvfoot.red.matches.displayable.BroadcasterRowDisplayable;
import io.oldering.tvfoot.red.util.plaid.ReflowText;
import io.reactivex.Observable;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static io.oldering.tvfoot.red.util.Preconditions.checkNotNull;

public class MatchActivity extends AppCompatActivity {
  private ActivityMatchBinding binding;
  @Inject FlowController flowController;
  @Inject MatchBinder binder;
  @Nullable private String matchId = null;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();
    final Uri uri = intent.getData();
    matchId = intent.getStringExtra("MATCH_ID");
    if (matchId == null && //
        uri != null && //
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
      flowController.toMatches();
    }

    binding = DataBindingUtil.setContentView(this, R.layout.activity_match);
    TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot());

    setupTransition();

    Timber.d("match with load with id %s", matchId);
    binder.bind();
  }

  private void setupTransition() {
    final TextView textView = binding.matchHeadline;
    textView.setTransitionName("reflowing");

    setEnterSharedElementCallback(new SharedElementCallback() {
      @Override
      public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements,
          List<View> sharedElementSnapshots) {
        ReflowText.setupReflow(getIntent(), textView);
      }

      @Override
      public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements,
          List<View> sharedElementSnapshots) {
        ReflowText.setupReflow(new ReflowText.ReflowableTextView(textView));
      }
    });
  }

  public Observable<MatchIntent> loadMatchIntent() {
    assert matchId != null;
    return Observable.just(MatchIntent.LoadMatch.create(matchId));
  }

  public void render(MatchViewState state) {
    switch (state.status()) {
      case MATCH_LOADING:
        renderMatchLoading();
        break;
      case MATCH_ERROR:
        renderError();
        break;
      case MATCH_LOADED:
        renderMatchLoaded(state);
        break;
    }
  }

  private void renderMatchLoading() {
    //binding.matchContainer.setVisibility(GONE);
    binding.errorView.setVisibility(GONE);
    //binding.progressBar.setVisibility(VISIBLE);
  }

  private void renderError() {
    //binding.matchContainer.setVisibility(GONE);
    binding.errorView.setVisibility(VISIBLE);
    //binding.progressBar.setVisibility(GONE);
  }

  private void renderMatchLoaded(MatchViewState state) {
    MatchDisplayable match =
        checkNotNull(state.match(), "match == null for renderMatchLoaded with state " + state);
    binding.setMatch(match);
    setupBroadcastersView(match.broadcasters());

    //binding.matchContainer.setVisibility(VISIBLE);
    binding.errorView.setVisibility(GONE);
    //binding.progressBar.setVisibility(GONE);
  }

  private void setupBroadcastersView(List<BroadcasterRowDisplayable> broadcasters) {
    BroadcastersAdapter broadcastersAdapter = new BroadcastersAdapter();
    broadcastersAdapter.addAll(broadcasters);
    binding.matchBroadcasters.setAdapter(broadcastersAdapter);
  }
}
