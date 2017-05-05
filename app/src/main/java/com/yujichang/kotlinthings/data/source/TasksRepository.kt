package com.yujichang.kotlinthings.data.source

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   : 喵喵
 *version: 1.0
 */
class TasksRepository private constructor() : TasksDataSource {

    companion object {
        fun getInstance(tasksRemoteDataSource: TasksDataSource
                        , tasksLocalDataSource: TasksDataSource)
                : TasksRepository = Inner.single
    }

    private object Inner {
        val single = TasksRepository()
    }


}