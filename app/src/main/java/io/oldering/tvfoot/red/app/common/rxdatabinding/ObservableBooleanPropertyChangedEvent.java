package io.oldering.tvfoot.red.app.common.rxdatabinding;

import android.databinding.ObservableBoolean;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class ObservableBooleanPropertyChangedEvent {
  @CheckResult @NonNull public static ObservableBooleanPropertyChangedEvent create(
      @NonNull ObservableBoolean observableField, int propertyId, boolean value) {
    return new AutoValue_ObservableBooleanPropertyChangedEvent(observableField, propertyId, value);
  }

  ObservableBooleanPropertyChangedEvent() {
  }

  public abstract ObservableBoolean field();

  public abstract int propertyId();

  public abstract boolean value();
}
