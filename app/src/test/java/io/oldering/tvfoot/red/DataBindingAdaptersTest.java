package io.oldering.tvfoot.red;

import android.view.View;
import io.oldering.tvfoot.red.app.common.DataBindingAdapters;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DataBindingAdaptersTest {
  @Test public void setVisibilityGone() {
    View view = mock(View.class);
    DataBindingAdapters.setVisibility(view, false);
    verify(view).setVisibility(View.GONE);
  }

  @Test public void setVisibilityVisible() {
    View view = mock(View.class);
    DataBindingAdapters.setVisibility(view, true);
    verify(view).setVisibility(View.VISIBLE);
  }
}
