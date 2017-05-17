package com.yujichang.kotlinthings.statistics

import com.yujichang.kotlinthings.data.Task
import com.yujichang.kotlinthings.data.source.TasksDataSource
import com.yujichang.kotlinthings.data.source.TasksRepository
import com.yujichang.kotlinthings.util.EspressoIdlingResource

/**
 *author : jichang
 *time   : 2017/05/17
 *desc   : complete
 *version: 1.0
 */
class StatisticsPresenter(val mTasksRepository: TasksRepository,
                          val mStatisticsView: StatisticsContract.View) : StatisticsContract.Presenter {
    init {
        mStatisticsView.setPresenter(this)
    }

    override fun start() {
        loadStatistics()
    }

    private fun loadStatistics() {
        mStatisticsView.setProgressIndicator(true)
        //网络请求可能会以不同的线程处理，所以请确保Espresso知道
        //应用程序正忙，直到响应处理。
        EspressoIdlingResource.increment() //应用程式很忙，直到另行通知

        mTasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                var activeTasks = 0
                var completedTasks = 0

                // 这个回调可能会被调用两次, 一次为缓存和一次来自加载
                //服务器API的数据, 所以我们在减量之前检查, 除此以外
                // 它抛出 "Counter has been corrupted!"例外。
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow) {
                    EspressoIdlingResource.decrement() //将应用设置为空闲
                }

                // 我们计算活动和已完成任务的数量
                for (task in tasks) {
                    if (task.completed) {
                        completedTasks += 1
                    } else {
                        activeTasks += 1
                    }
                }
                // 该视图可能无法处理UI更新
                if (!mStatisticsView.isActive) {
                    return
                }
                mStatisticsView.setProgressIndicator(false)

                mStatisticsView.showStatistics(activeTasks, completedTasks)
            }

            override fun onDataNotAvailable() {
                // 该视图可能无法处理UI更新
                if (!mStatisticsView.isActive) {
                    return
                }
                mStatisticsView.showLoadingStatisticsError()
            }

        })
    }
}