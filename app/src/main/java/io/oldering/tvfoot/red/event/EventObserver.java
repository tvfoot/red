package io.oldering.tvfoot.red.event;

import io.reactivex.Observer;

public abstract class EventObserver<T> implements Observer<Event<T>> {
  @Override public void onNext(Event<T> event) {
    switch (event.status) {
      case LOADING:
        onLoading(event);
        break;
      case IDLE:
        onIdle(event);
        break;
      case ERROR:
        onError(event);
        break;
    }
  }

  abstract void onLoading(Event<T> event);

  abstract void onIdle(Event<T> event);

  abstract void onError(Event<T> event);
}
