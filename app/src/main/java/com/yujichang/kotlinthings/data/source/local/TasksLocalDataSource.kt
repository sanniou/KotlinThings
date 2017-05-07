package com.yujichang.kotlinthings.data.source.local

import android.content.Context
import com.yujichang.kotlinthings.data.source.TasksDataSource

/**
 *author : jichang
 *time   : 2017/05/05
 *desc   :
 *具体实现数据源为db。
 *version: 1.0
 */
class TasksLocalDataSource : TasksDataSource {
    companion object {
        fun getInstance(context: Context): TasksLocalDataSource
                = Inner.single
    }

    private object Inner {
        val single = TasksLocalDataSource()
    }
}