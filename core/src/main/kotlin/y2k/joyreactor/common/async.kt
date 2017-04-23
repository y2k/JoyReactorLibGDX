package y2k.joyreactor.common

import java8.util.concurrent.CompletableFuture
import kotlinx.coroutines.experimental.future.await
import kotlinx.coroutines.experimental.future.future

/**
 * Created by y2k on 03/12/2016.
 **/

//fun asyncGdx(
//    coroutine c: CoroutineContext<Unit>.() -> Continuation<Unit>
//) {
//    async({ Gdx.app.postRunnable(it) }, c)
//}
//
//public fun <T> defer(context: CoroutineContext, block: suspend CoroutineScope.() -> T) : Deferred<T> =
//    async(context, block = block)
//
//public fun <T> future(context: CoroutineContext = CommonPool, block: suspend () -> T): CompletableFuture<T> {
//    val newContext = newCoroutineContext(CommonPool + context)
//    val job = Job(newContext[Job])
//    val future = CompletableFutureCoroutine<T>(newContext + job)
//    job.cancelFutureOnCompletion(future)
//    future.whenComplete { _, exception -> job.cancel(exception) }
//    block.startCoroutine(future)
//    return future
//}

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
fun <T> asyncGdx(f: suspend () -> T) {
    future(block = f)
}

@Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING")
inline suspend fun <T> await(future: CompletableFuture<T>): T {
    return future.await()
}