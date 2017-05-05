package com.yujichang.kotlinthings.util

import android.support.test.espresso.IdlingResource

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : Contains a static reference to @see IdlingResource, only available in the 'mock' build type.
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