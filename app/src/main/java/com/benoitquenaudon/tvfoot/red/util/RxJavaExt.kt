package com.benoitquenaudon.tvfoot.red.util

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.functions.Predicate


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
