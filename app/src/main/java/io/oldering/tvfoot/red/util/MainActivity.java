package io.oldering.tvfoot.red.util;

import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import javax.annotation.Nullable;
import javax.inject.Inject;

public class MainActivity extends BaseActivity {
  @Inject FlowController flowController;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivityComponent().inject(this);

    FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);

    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.DESTINATION, "Mars");
    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

    flowController.toMatches();
    finish();
  }
}