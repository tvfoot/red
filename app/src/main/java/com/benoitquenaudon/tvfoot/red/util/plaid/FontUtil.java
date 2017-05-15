/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.benoitquenaudon.tvfoot.red.util.plaid;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Adapted from github.com/romannurik/muzei/
 * <p/>
 * Also see https://code.google.com/p/android/issues/detail?id=9904
 */
public class FontUtil {

  private FontUtil() {
  }

  private static final Map<String, Typeface> typefaceCache = new HashMap<>();

  public static Typeface get(Context context, String font) {
    synchronized (typefaceCache) {
      if (!typefaceCache.containsKey(font)) {
        Typeface tf = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
            "fonts/" + font + ".ttf");
        typefaceCache.put(font, tf);
      }
      return typefaceCache.get(font);
    }
  }

  public static String getName(@NonNull Typeface typeface) {
    for (Map.Entry<String, Typeface> entry : typefaceCache.entrySet()) {
      if (entry.getValue().equals(typeface)) {
        return entry.getKey();
      }
    }
    return null;
  }
}