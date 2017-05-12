package com.yujichang.kotlinthings.util

import android.support.test.espresso.IdlingResource

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   :complete
 * 包含对@see IdlingResource的静态引用，仅在'mock'构建类型中可用。
 *version: 1.0
 */
object EspressoIdlingResource{

    private val RESOURCE = "GLOBAL"

    private val mCountingIdlingResource = SimpleCountingIdlingResource(RESOURCE)

    fun increment() {
        mCountingIdlingResource.increment()
    }

    fun decrement() {
        mCountingIdlingResource.decrement()
    }

    fun getIdlingResource(): IdlingResource {
        return mCountingIdlingResource
    }
}