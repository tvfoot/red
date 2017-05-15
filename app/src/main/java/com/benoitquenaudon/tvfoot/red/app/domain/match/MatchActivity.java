package com.benoitquenaudon.tvfoot.red.app.domain.match;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.SharedElementCallback;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.benoitquenaudon.rxdatabinding.databinding.RxObservableBoolean;
import com.benoitquenaudon.tvfoot.red.R;
import com.benoitquenaudon.tvfoot.red.RedAppConfig;
import com.benoitquenaudon.tvfoot.red.app.common.BaseActivity;
import com.benoitquenaudon.tvfoot.red.app.common.flowcontroller.FlowController;
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchIntent;
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchStateBinder;
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchViewState;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.BroadcastersAdapter;
import com.benoitquenaudon.tvfoot.red.databinding.ActivityMatchBinding;
import com.benoitquenaudon.tvfoot.red.util.plaid.ReflowText;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import timber.log.Timber;

import static com.benoitquenaudon.tvfoot.red.app.common.PreConditions.checkNotNull;

public class MatchActivity extends BaseActivity {
  @Inject BroadcastersAdapter broadcastersAdapter;
  @Inject FlowController flowController;
  @Inject MatchStateBinder stateBinder;
  @Inject CompositeDisposable disposables;
  @Inject MatchViewModel viewModel;

  private ActivityMatchBinding binding;
  @Nullable private MatchDisplayable match = null;
  @Nullable private String matchId = null;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivityComponent().inject(this);

    Intent intent = getIntent();
    match = intent.getParcelableExtra("MATCH");
    if (match != null) {
      Timber.d("match %s", match);
      initViewModel(match);
      matchId = match.matchId();
    } else {
      Uri uri = intent.getData();
      matchId = extractMatchId(uri);

      if (matchId == null) {
        Timber.w("match id is null %s", uri);
        Toast.makeText(this, "match id is null with uri " + uri, Toast.LENGTH_LONG).show();
        flowController.toMatches();
        finish();
        return;
      } else {
        Timber.d("matchId %s", matchId);
      }
    }

    setupView();

    disposables = new CompositeDisposable();
    bind();
  }

  private void setupView() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_match);
    binding.matchDetailBroadcasters.setAdapter(broadcastersAdapter);
    binding.setViewModel(viewModel);

    setupTransition();
  }

  @Nullable private String extractMatchId(Uri uri) {
    if (RedAppConfig.AUTHORITIES.contains(uri.getAuthority()) && //
        RedAppConfig.SCHEMES.contains(uri.getScheme())) {
      final List<String> segments = uri.getPathSegments();
      if (segments != null && //
          segments.size() == 5 && //
          RedAppConfig.PATH_MATCH.equals(segments.get(0))) {
        return segments.get(4);
      }
    }
    return null;
  }

  private void initViewModel(MatchDisplayable match) {
    viewModel.match.set(match);
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

  private void bind() {
    disposables.add(stateBinder.getStatesAsObservable().subscribe(this::render));
    stateBinder.forwardIntents(intents());

    disposables.add(RxObservableBoolean.propertyChanges(viewModel.shouldNotifyMatchStart)
        .subscribe(shouldNotifyMatchStart -> {
          if (shouldNotifyMatchStart) {
            Snackbar.make(binding.getRoot(),
                "You will be notified 5 minutes before the game starts", Snackbar.LENGTH_LONG)
                .show();
          } else {
            // TODO(benoit) hide if one is displayed. Would happen on sequential taps
          }
        }));
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    disposables.dispose();
  }

  public Observable<MatchIntent> intents() {
    return Observable.merge(initialIntent(), fabClickIntent());
  }

  private Observable<MatchIntent.InitialIntent> initialIntent() {
    MatchIntent.InitialIntent matchIntent;
    if (match != null) {
      matchIntent = MatchIntent.InitialIntent.withMatch(match);
    } else {
      assert matchId != null;
      matchIntent = MatchIntent.InitialIntent.withMatchId(matchId);
    }
    return Observable.just(matchIntent);
  }

  private Observable<MatchIntent.NotifyMatchStartIntent> fabClickIntent() {
    return RxView.clicks(binding.notifyMatchStartFab)
        .map(ignored -> MatchIntent.NotifyMatchStartIntent.create(
            checkNotNull(matchId, "MatchId is null"), !isMatchNotificationActivated()));
  }

  private boolean isMatchNotificationActivated() {
    return binding.notifyMatchStartFab.isActivated();
  }

  public void render(MatchViewState state) {
    viewModel.updateFromState(state);
  }
}
