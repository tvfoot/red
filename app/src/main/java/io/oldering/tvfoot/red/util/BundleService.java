package io.oldering.tvfoot.red.util;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class BundleService {
    private final Bundle data;

    public BundleService(@Nullable Bundle savedState, @Nullable Bundle intentExtras) {
        data = new Bundle();

        if (savedState != null) {
            data.putAll(savedState);
        }
        if (intentExtras != null) {
            data.putAll(intentExtras);
        }
    }

    public Bundle getAll() {
        return data;
    }

    public Parcelable getParcelable(String bundleKey) {
        return data.getParcelable(bundleKey);
    }
}
