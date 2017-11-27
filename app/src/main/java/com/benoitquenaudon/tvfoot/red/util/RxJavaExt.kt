package com.benoitquenaudon.tvfoot.red.util

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.functions.Predicate
import io.reactivex.internal.functions.Functions


fun <T> Predicate<T>.negate(): Predicate<T> = Predicate { t -> !test(t) }

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T, U> Observable<T>.notOfType(clazz: Class<U>): Observable<T> {
  checkNotNull(clazz) { "clazz is null" }
  return filter(Functions.isInstanceOf<T, U>(clazz).negate())
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <U, T : Iterable<U>> Single<T>.flatMapIterable(): Observable<U> {
  return this.flatMapObservable {
    Observable.fromIterable(it)
  }
}