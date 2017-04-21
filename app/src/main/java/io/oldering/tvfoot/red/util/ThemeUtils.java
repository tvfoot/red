package io.oldering.tvfoot.red.util;

import android.content.Context;
import android.util.TypedValue;
import io.oldering.tvfoot.red.R;

public final class ThemeUtils {
  private static final ThreadLocal<TypedValue> TYPED_VALUE = new ThreadLocal<TypedValue>() {
    @Override protected TypedValue initialValue() {
      return new TypedValue();
    }
  };

  private ThemeUtils() {
  }

  public static void ensureRuntimeTheme(Context context) {
    final TypedValue typedValue = TYPED_VALUE.get();
    context.getTheme().resolveAttribute(R.attr.ltRuntimeTheme, typedValue, true);
    if (typedValue.resourceId <= 0) {
      throw new IllegalArgumentException("runtimeTheme not defined in the preview theme");
    }
    context.setTheme(typedValue.resourceId);
  }
}