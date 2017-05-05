package com.yujichang.kotlinthings.util

import android.support.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : 喵喵
 *version: 1.0
 */

/**
 * Creates a SimpleCountingIdlingResource
 *
 * @param resourceName the resource name this resource should report to Espresso.
 */
class SimpleCountingIdlingResource(val resourceName: String) : IdlingResource {


    private val counter = AtomicInteger(0)

    // written from main thread, read from any thread.
    lateinit @Volatile private var resourceCallback: IdlingResource.ResourceCallback

    override fun getName() = resourceName

    override fun isIdleNow() = counter.get() == 0

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = resourceCallback
    }

    /**
     * Increments the count of in-flight transactions to the resource being monitored.
     */
    fun increment() = counter.getAndIncrement()

    /**
     * Decrements the count of in-flight transactions to the resource being monitored.

     * If this operation results in the counter falling below 0 - an exception is raised.

     * @throws IllegalStateException if the counter is below 0.
     */
    fun decrement() {
        val counterVal = counter.decrementAndGet()
        if (counterVal == 0) {
            // we've gone from non-zero to zero. That means we're idle now! Tell espresso.
            resourceCallback.onTransitionToIdle()
        }

        if (counterVal < 0) {
            throw IllegalArgumentException("Counter has been corrupted!")
        }
    }

}