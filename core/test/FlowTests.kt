import java8.util.concurrent.Flow
import java8.util.concurrent.ForkJoinPool
import java8.util.concurrent.SubmissionPublisher
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by y2k on 10/02/2017.
 **/
class FlowTests {

    @Test
    fun `test default parallelism level`() {
        val number = ForkJoinPool.getCommonPoolParallelism()
        assertTrue("getCommonPoolParallelism = $number", number > 1)
    }

    @Test
    fun `SubmissionPublisher tests`() {
        SubmissionPublisher<String>()
            .map(String::toInt)
            .flatMap { SubmissionPublisher<Int>() }
            .subscribe {
                println("item = $it")
            }
    }
}

fun <T, R> Flow.Publisher<T>.flatMap(f: (T) -> Flow.Publisher<R>): Flow.Publisher<R>
    = map(f).merge()

private fun <T> Flow.Publisher<Flow.Publisher<T>>.merge(): Flow.Publisher<T> {
    val result = SubmissionPublisher<T>()
    subscribe(object : Flow.Subscriber<Flow.Publisher<T>> {
        override fun onNext(item: Flow.Publisher<T>) {
            item.subscribe(object : Flow.Subscriber<T> {

                override fun onNext(item: T) {
                    result.submit(item)
                }

                override fun onSubscribe(subscription: Flow.Subscription?) = Unit
                override fun onComplete() = result.close()
                override fun onError(throwable: Throwable?) = result.closeExceptionally(throwable)
            })
        }

        override fun onSubscribe(subscription: Flow.Subscription?) = Unit
        override fun onComplete() = result.close()
        override fun onError(throwable: Throwable?) = result.closeExceptionally(throwable)
    })
    return result
}

fun <T, R> Flow.Publisher<T>.map(f: (T) -> R): Flow.Publisher<R> {
    val result = SubmissionPublisher<R>()
    subscribe(object : Flow.Subscriber<T> {

        override fun onNext(item: T) {
            result.submit(f(item))
        }

        override fun onSubscribe(subscription: Flow.Subscription?) = Unit
        override fun onComplete() = result.close()
        override fun onError(throwable: Throwable?) = result.closeExceptionally(throwable)
    })
    return result
}

fun <T> Flow.Publisher<T>.subscribe(f: (T) -> Unit) {
    subscribe(object : Flow.Subscriber<T> {

        override fun onNext(item: T) = f(item)
        override fun onError(throwable: Throwable?): Unit = TODO("not implemented")
        override fun onComplete() = Unit
        override fun onSubscribe(subscription: Flow.Subscription?) = Unit
    })
}