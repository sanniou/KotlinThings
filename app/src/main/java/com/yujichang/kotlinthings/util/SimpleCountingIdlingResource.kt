package com.yujichang.kotlinthings.util

import android.support.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   :complete
 * 一个简单的计数器实现使用{@link IdlingResource}确定闲置通过
 * 维持一个内部计数器. 当计数器为 0 - 它被认为是空闲的, 当
 * 非零，它不空闲. 这与{@link java.util.concurrent.Semaphore}的方式非常相似
 *行为。
 * <p>
 * 这个类可以用来包装操作当在访问UI是进行测试.
 *version: 1.0
 */

/**
 * 创建一个SimpleCountingIdlingResource
 *
 * @param resourceName 该资源应该向Espresso报告的资源名称。
 */
class SimpleCountingIdlingResource(val resourceName: String) : IdlingResource {


    private val counter = AtomicInteger(0)

    // 从主线程写入，从任何线程读取.
     @Volatile private var resourceCallback: IdlingResource.ResourceCallback?=null

    override fun getName() = resourceName

    override fun isIdleNow() = counter.get() == 0

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
       resourceCallback = callback
    }

    /**
     * 将正在运行的事务的计数增加到正在监视的资源。
     */
    fun increment() = counter.getAndIncrement()

    /**
     *减少正在监视的资源的数量。

     * 如果此操作导致计数器低于0  - 引发异常。

     * @throws IllegalStateException 如果计数器低于0.
     */
    fun decrement() {
        val counterVal = counter.decrementAndGet()
        if (counterVal == 0) {
            // we've gone from non-zero to zero. That means we're idle now! Tell espresso.
            resourceCallback?.onTransitionToIdle()
        }

        if (counterVal < 0) {
            throw IllegalArgumentException("Counter has been corrupted!")
        }
    }

}