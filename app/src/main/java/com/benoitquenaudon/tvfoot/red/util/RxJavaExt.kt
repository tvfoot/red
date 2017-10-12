package com.benoitquenaudon.tvfoot.red.util

import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.functions.Predicate
import io.reactivex.internal.functions.Functions
import io.reactivex.internal.functions.ObjectHelper


fun <T> Predicate<T>.negate(): Predicate<T> {
  return Predicate { t -> !test(t) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T, U> Observable<T>.notOfType(clazz: Class<U>): Observable<T> {
  ObjectHelper.requireNonNull(clazz, "clazz is null")
  return filter(Functions.isInstanceOf<T, U>(clazz).negate())
}
