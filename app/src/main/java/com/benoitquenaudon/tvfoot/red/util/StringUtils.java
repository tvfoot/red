package com.benoitquenaudon.tvfoot.red.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

public class StringUtils {
  private StringUtils() {
    throw new RuntimeException("Can't touch this");
  }

  public static String capitalize(@Nullable String string) {
    if (TextUtils.isEmpty(string)) {
      return string;
    } else {
      char ch = string.charAt(0);
      return Character.isTitleCase(ch) ? string : Character.toTitleCase(ch) + string.substring(1);
    }
  }
}
