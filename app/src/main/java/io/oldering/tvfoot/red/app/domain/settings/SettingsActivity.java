package io.oldering.tvfoot.red.app.domain.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import io.oldering.tvfoot.red.BuildConfig;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.app.common.AppCompatPreferenceActivity;
import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.pref_settings);
    setupActionBar();

    loadVersion();
  }

  private void loadVersion() {
    findPreference("pref_key_app_version").setSummary(
        String.format("%s (%s)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
  }

  /**
   * Set up the {@link android.app.ActionBar}, if the API is available.
   */
  private void setupActionBar() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override public boolean onMenuItemSelected(int featureId, MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      if (!super.onMenuItemSelected(featureId, item)) {
        NavUtils.navigateUpFromSameTask(this);
      }
      return true;
    }
    return super.onMenuItemSelected(featureId, item);
  }

  /** {@inheritDoc} */
  @Override public boolean onIsMultiPane() {
    return isXLargeTablet(this);
  }

  /**
   * Helper method to determine if the device has an extra-large screen. For
   * example, 10" tablets are extra-large.
   */
  private static boolean isXLargeTablet(Context context) {
    return (context.getResources().getConfiguration().screenLayout
        & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
  }

  /** {@inheritDoc} */
  @Override @TargetApi(Build.VERSION_CODES.HONEYCOMB) public void onBuildHeaders(
      List<Header> target) {
    // uncommend when need using headers
    // loadHeadersFromResource(R.xml.pref_headers, target);
  }

  /**
   * This method stops fragment injection in malicious applications.
   * Make sure to deny any unknown fragments here.
   */
  protected boolean isValidFragment(String fragmentName) {
    return PreferenceFragment.class.getName().equals(fragmentName)
        || SettingsPreferenceFragment.class.getName().equals(fragmentName);
  }

  /**
   * NOT USED ATM: will be needed if we start using headers
   * This fragment shows general preferences only. It is used when the
   * activity is showing a two-pane settings UI.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB) //
  public static class SettingsPreferenceFragment extends PreferenceFragment {
    @Override public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_settings);
      setHasOptionsMenu(true);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
      if (id == android.R.id.home) {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
        return true;
      }
      return super.onOptionsItemSelected(item);
    }
  }
}
