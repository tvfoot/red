package io.oldering.tvfoot.red.app.common.rxdatabinding;

import android.databinding.ObservableBoolean;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import io.reactivex.Observable;

import static io.oldering.tvfoot.red.app.common.PreConditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for {@link
 * ObservableBoolean}.
 */
public final class RxObservableBoolean {

  @CheckResult @NonNull
  public static Observable<ObservableBooleanPropertyChangedEvent> propertyChangedEvents(
      @NonNull ObservableBoolean field) {
    checkNotNull(field, "field == null");
    return new ObservableBooleanObservable(field);
  }

  private RxObservableBoolean() {
    throw new AssertionError("No instances.");
  }
}
