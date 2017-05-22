package com.benoitquenaudon.tvfoot.red.app.domain.match;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
      Timber.w("matchDisplayable id is null %s", uri);
      Toast.makeText(this, "matchDisplayable id is null with uri " + uri, Toast.LENGTH_LONG).show();
      flowController.toMatches();
      finish();
      return;
    }

    binding = DataBindingUtil.setContentView(this, R.layout.activity_match);
    binding.matchDetailBroadcasters.setAdapter(broadcastersAdapter);
    binding.setViewModel(viewModel);

    Timber.d("matchDisplayable with load with id %s", matchId);
    disposables = new CompositeDisposable();
    bind();
  }

  private void bind() {
    disposables.add(stateBinder.getStatesAsObservable().subscribe(this::render));
    stateBinder.forwardIntents(intents());

    disposables.add(RxObservableBoolean.propertyChanges(viewModel.shouldNotifyMatchStart)
        .subscribe(shouldNotifyMatchStart -> {
          if (shouldNotifyMatchStart) {
            Snackbar.make(binding.getRoot(),
                "You will be notified 10 minutes before the game starts", Snackbar.LENGTH_LONG)
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
    return Observable.just(
        MatchIntent.InitialIntent.create(checkNotNull(matchId, "MatchId is null")));
  }

  private Observable<MatchIntent.NotifyMatchStartIntent> fabClickIntent() {
    return RxView.clicks(binding.notifyMatchStartFab)
        .map(ignored -> MatchIntent.NotifyMatchStartIntent.create(viewModel.match.get().matchId(),
            viewModel.match.get().startAt(), !isMatchNotificationActivated()));
  }

  private boolean isMatchNotificationActivated() {
    return binding.notifyMatchStartFab.isActivated();
  }

  public void render(MatchViewState state) {
    viewModel.updateFromState(state);
  }
}
