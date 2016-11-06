package io.oldering.tvfoot.red.util;

import android.text.TextUtils;

public class StringUtils {
    private StringUtils() {
        throw new RuntimeException("Can't touch this");
    }

    public static String capitalize(String string) {
        if (TextUtils.isEmpty(string)) {
            return string;
        } else {
            char ch = string.charAt(0);
            return Character.isTitleCase(ch) ? string : Character.toTitleCase(ch) + string.substring(1);
        }
    }
}
