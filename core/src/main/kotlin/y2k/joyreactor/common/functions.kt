package y2k.joyreactor.common

/**
* Created by y2k on 29/11/2016.
**/

fun <T1, R> Function1<T1, R>.partial(t1: T1): Function0<R>
    = { invoke(t1) }

fun <T1, T2, R> Function2<T1, T2, R>.partial(t1: T1, t2: T2): Function0<R>
    = { invoke(t1, t2) }

fun <T1, T2, R> Function2<T1, T2, R>.partial(t1: T1): Function1<T2, R>
    = { invoke(t1, it) }

fun <T1, T2, T3, R> Function3<T1, T2, T3, R>.partial(t1: T1, t2: T2): Function1<T3, R>
    = { invoke(t1, t2, it) }

fun <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R>.partial(t1: T1, t2: T2): Function2<T3, T4, R>
    = { t3, t4 -> invoke(t1, t2, t3, t4) }