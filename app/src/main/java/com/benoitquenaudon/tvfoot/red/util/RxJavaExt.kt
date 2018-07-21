package com.benoitquenaudon.tvfoot.red.util

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import io.reactivex.internal.functions.Functions

fun <T : Any> Predicate<T>.negate(): Predicate<T> = Predicate { t -> !test(t) }

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T : Any, U : Any> Observable<T>.notOfType(clazz: Class<U>): Observable<T> {
  return filter(Predicate(clazz::isInstance).negate())
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <U : Any, T : Iterable<U>> Single<T>.flatMapIterable(): Observable<U> {
  return this.flatMapObservable {
    Observable.fromIterable(it)
  }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T> Observable<T>.errorHandlingSubscribe(
  onNext: (T) -> Unit
): Disposable {
  return subscribe(
      Consumer(onNext),
      Consumer { t -> throw OnErrorNotImplementedException(t) },
      Action {})
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T> Observable<T>.errorHandlingSubscribe(): Disposable {
  return subscribe(
      Functions.emptyConsumer(),
      Consumer { t -> throw OnErrorNotImplementedException(t) },
      Functions.EMPTY_ACTION
  )
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T> Maybe<T>.errorHandlingSubscribe(
  onNext: (T) -> Unit
): Disposable = subscribe(
    Consumer(onNext),
    Consumer { t -> throw OnErrorNotImplementedException(t) })
