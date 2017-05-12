package com.yujichang.kotlinthings

/**
 *author : jichang
 *time   : 2017/05/03
 *desc   : 喵喵
 *version: 1.0
 */
interface BaseView<in T> {
    fun setPresenter(presenter: T)
}