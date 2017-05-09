package io.oldering.tvfoot.red.app.common.rxdatabinding;

import android.databinding.ObservableBoolean;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.oldering.tvfoot.red.app.common.PreConditions.checkMainThread;

public class ObservableBooleanObservable extends Observable<ObservableBooleanPropertyChangedEvent> {
  private final ObservableBoolean field;

  public ObservableBooleanObservable(ObservableBoolean observableField) {
    this.field = observableField;
  }

  @Override
  protected void subscribeActual(Observer<? super ObservableBooleanPropertyChangedEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(field, observer);
    observer.onSubscribe(listener);
    field.addOnPropertyChangedCallback(listener.onPropertyChangedCallback);
  }

  private final class Listener extends MainThreadDisposable {
    private final ObservableBoolean observableField;
    private final android.databinding.Observable.OnPropertyChangedCallback
        onPropertyChangedCallback;

    Listener(ObservableBoolean observableField,
        final Observer<? super ObservableBooleanPropertyChangedEvent> observer) {
      this.observableField = observableField;
      this.onPropertyChangedCallback =
          new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override public void onPropertyChanged(android.databinding.Observable observableField,
                int propertyId) {
              if (!isDisposed()) {
                observer.onNext(ObservableBooleanPropertyChangedEvent.create(
                    (ObservableBoolean) observableField, propertyId,
                    ((ObservableBoolean) observableField).get()));
              }
            }
          };
    }

    @Override protected void onDispose() {
      observableField.removeOnPropertyChangedCallback(onPropertyChangedCallback);
    }
  }
}
