package io.oldering.tvfoot.red.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import io.oldering.tvfoot.red.flowcontroller.FlowController;
import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);

    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.DESTINATION, "Mars");
    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

    FlowController.toMatchs(this);
    finish();
  }
}