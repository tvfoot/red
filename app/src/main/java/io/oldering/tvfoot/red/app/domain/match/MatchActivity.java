package io.oldering.tvfoot.red.app.domain.match;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
import com.jakewharton.rxbinding2.view.RxView;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.RedAppConfig;
import io.oldering.tvfoot.red.app.common.BaseActivity;
import io.oldering.tvfoot.red.app.common.flowcontroller.FlowController;
import io.oldering.tvfoot.red.app.common.rxdatabinding.ObservableBooleanPropertyChangedEvent;
import io.oldering.tvfoot.red.app.common.rxdatabinding.RxObservableBoolean;
import io.oldering.tvfoot.red.app.domain.match.state.MatchIntent;
import io.oldering.tvfoot.red.app.domain.match.state.MatchStateBinder;
import io.oldering.tvfoot.red.app.domain.match.state.MatchViewState;
import io.oldering.tvfoot.red.app.domain.matches.BroadcastersAdapter;
import io.oldering.tvfoot.red.databinding.ActivityMatchBinding;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import timber.log.Timber;

import static io.oldering.tvfoot.red.app.common.PreConditions.checkNotNull;

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
      Timber.w("match id is null %s", uri);
      Toast.makeText(this, "match id is null with uri " + uri, Toast.LENGTH_LONG).show();
      flowController.toMatches();
      finish();
      return;
    }

    binding = DataBindingUtil.setContentView(this, R.layout.activity_match);
    binding.matchDetailBroadcasters.setAdapter(broadcastersAdapter);
    binding.setViewModel(viewModel);

    Timber.d("match with load with id %s", matchId);
    disposables = new CompositeDisposable();
    bind();
  }

  private void bind() {
    disposables.add(stateBinder.getStatesAsObservable().subscribe(this::render));
    stateBinder.forwardIntents(intents());

    disposables.add(RxObservableBoolean.propertyChangedEvents(viewModel.shouldNotifyMatchStart)
        .map(ObservableBooleanPropertyChangedEvent::value)
        .subscribe(shouldNotifyMatchStart -> Snackbar.make(binding.getRoot(),
            shouldNotifyMatchStart ? "You will be notified" : "Noop", Snackbar.LENGTH_LONG)
            .show()));
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
