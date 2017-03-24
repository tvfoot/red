package io.oldering.tvfoot.red.match;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.RedAppConfig;
import io.oldering.tvfoot.red.databinding.ActivityMatchBinding;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import io.oldering.tvfoot.red.util.BaseActivity;
import io.reactivex.Observable;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import timber.log.Timber;

public class MatchActivity extends BaseActivity {
  private ActivityMatchBinding binding;
  @Inject FlowController flowController;
  @Inject MatchBinder binder;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivityComponent().inject(this);

    @Nullable String matchId = null;
    final Uri uri = getIntent().getData();
    if (uri != null) {
      if (RedAppConfig.AUTHORITIES.contains(uri.getAuthority()) && //
          RedAppConfig.SCHEMES.contains(uri.getScheme())) {
        final List<String> segments = uri.getPathSegments();
        if (segments != null && //
            segments.size() == 2 && //
            RedAppConfig.PATH_MATCH.equals(segments.get(0))) {
          matchId = segments.get(1);
        }
      }
    }

    if (matchId == null) {
      Timber.w("match id is null", uri);
      flowController.toMatches();
    }

    binding = DataBindingUtil.setContentView(this, R.layout.activity_match);

    Timber.d("match with load with id %s", matchId);
    binder.bind();
  }

  public Observable<MatchIntent> loadMatchIntent() {
    return Observable.just(MatchIntent.LoadMatch.create());
  }

  public void render(MatchViewState state) {

  }
}
