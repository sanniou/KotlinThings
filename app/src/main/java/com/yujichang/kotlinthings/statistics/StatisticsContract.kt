package com.yujichang.kotlinthings.statistics

import com.yujichang.kotlinthings.BasePresenter
import com.yujichang.kotlinthings.BaseView

/**
 *author : jichang
 *time   : 2017/05/17
 *desc   : complete
 *version: 1.0
 */
interface StatisticsContract{

    interface View : BaseView<Presenter> {

        fun setProgressIndicator(active: Boolean)

        fun showStatistics(numberOfIncompleteTasks: Int, numberOfCompletedTasks: Int)

        fun showLoadingStatisticsError()

        val isActive: Boolean
    }

    interface Presenter : BasePresenter
}