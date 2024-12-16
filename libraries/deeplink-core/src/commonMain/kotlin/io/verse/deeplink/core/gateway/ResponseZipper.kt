package io.verse.deeplink.core.gateway

import io.tagd.langx.ref.concurrent.atomic.AtomicBoolean
import io.tagd.langx.ref.concurrent.atomic.AtomicInteger

class ResponseZipper<KEY, T, U>(
    val size: Int = 0,
    val eagerlyExecuteDoneOnSuccess: Boolean = false,
    private val executeOnDone: ((results: Results<KEY, T, U>) -> Unit)? = null,
) {

    private var counter: AtomicInteger = AtomicInteger(0)
    private var invoked: AtomicBoolean = AtomicBoolean(false)
    private val results = Results<KEY, T, U>()

    fun success(key: KEY, result: T) {
        counter.getAndIncrement()
        results.successes[key] = result
        invokeCallbackEagerlyIfDone()
    }

    fun upgrade(key: KEY, result: U) {
        counter.getAndIncrement()
        results.upgrades[key] = result
        invokeCallback()
    }

    fun failure(key: KEY, result: Throwable) {
        counter.getAndIncrement()
        results.failures[key] = result
        invokeCallback()
    }

    private fun invokeCallbackEagerlyIfDone() {
        if ((counter.get() == size || eagerlyExecuteDoneOnSuccess) && !invoked.get()) {
            invoked.set(true)
            executeOnDone?.invoke(results)
        }
    }

    private fun invokeCallback() {
        if ((counter.get() == size) && !invoked.get()) {
            invoked.set(true)
            executeOnDone?.invoke(results)
        }
    }

    data class Results<KEY, T, U>(
        val successes: HashMap<KEY, T> = hashMapOf(),
        val upgrades: HashMap<KEY, U> = hashMapOf(),
        val failures: HashMap<KEY, Throwable> = hashMapOf()
    )
}