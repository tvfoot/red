package io.oldering.tvfoot.red.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import dagger.android.AndroidInjection;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import javax.annotation.Nullable;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
  @Inject FlowController flowController;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    ThemeUtils.ensureRuntimeTheme(this);
    super.onCreate(savedInstanceState);

    FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);

    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.DESTINATION, "Mars");
    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

    flowController.toMatches();
    finish();
  }
}