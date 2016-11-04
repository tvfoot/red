package io.oldering.tvfoot.red.util.string;

import android.text.TextUtils;

public class StringUtils {
    private StringUtils() {
        throw new RuntimeException("Can't touch me");
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
